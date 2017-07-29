package project.kg.stamina.player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import me.kg.filedata.DataHandler;
import project.kg.stamina.player.behavior.BehaviorType;
import project.kg.stamina.player.behavior.MotionCalculator;
import project.kg.stamina.set.CONFIG;

public class GameData
{

	public static CONFIG cfg;
	private static DecimalFormat df = new DecimalFormat("#.00");

	private boolean isWeak = false;
	private boolean isExhausted = false;

	protected boolean isSaving = false;
	public MotionCalculator motionHandler = new MotionCalculator();

	public final String playerName;
	public final Player thePlayer;

	protected double pp;
	protected double maxPP;

	protected double changePP = 0.0;

	public GameData(Player p)
	{
		thePlayer = p;
		playerName = p.getName();
	}

	public void refreshHook()
	{
		if (cfg.HookExpBar)
		{
			int level = (int) pp;
			thePlayer.setLevel(level);
			thePlayer.setExp(pp == maxPP ? 1f : (float) (pp - level));
		}
	}

	public void setMaxPP(double newPP)
	{
		if (newPP != this.maxPP)
		{
			this.maxPP = newPP;
			refreshHook();
			save();
		}
	}

	public void setPP(double newPP)
	{
		if (newPP != pp)
		{
			double lastPP = pp;

			if (newPP >= maxPP)
				this.pp = maxPP;
			else if (newPP <= 0)
				this.pp = 0;
			else
				this.pp = newPP;

			changePP += pp - lastPP;

			refreshHook();
			checkChange();
			save();
		}
	}

	public void decreasePP(double dpp)
	{
		if (dpp == 0)
			return;
		this.setPP(pp - dpp);
	}

	public void increasePP(double dpp)
	{
		if (dpp == 0)
			return;
		this.setPP(pp + dpp);
	}

	public double getPP()
	{
		return pp;
	}

	public double getMaxPP()
	{
		return maxPP;
	}

	public void join()
	{
	}

	public void quit()
	{
	}

	public void save()
	{
		if (!isSaving)
			DataSaver.addTask(this);
	}

	protected void update()
	{
		if (pp < cfg.ExhaustedThreshold)
		{
			if (isWeak)
				isWeak = false;
			if (!isExhausted)
				isExhausted = true;
			for (PotionEffect eff : cfg.exhaustedBuffs)
			{
				boolean flag = false;
				for (PotionEffect aeff : thePlayer.getActivePotionEffects())
					if (aeff.getType().equals(eff.getType()))
					{
						if (aeff.getDuration() <= eff.getDuration())
						{
							flag = false;
						} else
						{
							flag = true;
						}
						break;
					}
				if (!flag)
					thePlayer.addPotionEffect(eff, true);
			}
		} else if (pp < cfg.WeakThreshold)
		{
			if (isExhausted)
				isExhausted = false;
			if (!isWeak)
				isWeak = true;
			for (PotionEffect eff : cfg.weakBuffs)
			{
				boolean flag = false;
				for (PotionEffect aeff : thePlayer.getActivePotionEffects())
					if (aeff.getType().equals(eff.getType()))
					{
						if (aeff.getDuration() <= eff.getDuration())
						{
							flag = false;
						} else
						{
							flag = true;
						}
						break;
					}
				if (!flag)
					thePlayer.addPotionEffect(eff, true);
			}
		} else
		{
			if (isExhausted)
				isExhausted = false;
			if (isWeak)
				isWeak = false;
		}
	}

	public ArrayList<BehaviorType> getLimits()
	{
		if (isExhausted)
			return cfg.exhaustedLimit;
		if (isWeak)
			return cfg.weakLimit;
		return null;
	}

	public boolean canLimit()
	{
		return isExhausted || isWeak;
	}

	public boolean limited(BehaviorType type)
	{
		return canLimit() && getLimits().contains(type);
	}

	protected void tick()
	{
		double newPP = pp;

		double moveCost = motionHandler.calculate();

		newPP -= moveCost;

		boolean shouldRefection = true;
		if (cfg.RecoverWhenStill && moveCost != 0)
			shouldRefection = false;
		if (shouldRefection)
			if (newPP < maxPP && thePlayer.isOnGround())
			{
				newPP += cfg.TickRefection;
				if (newPP >= maxPP)
					newPP = maxPP;
			}

		if (newPP != pp)
		{
			if (newPP < 0)
				newPP = 0;
			changePP += newPP - pp;
			pp = newPP;
			refreshHook();
			save();
			checkChange();
		}
	}

	private void checkChange()
	{
		if (cfg.Hint)
			if (Math.abs(changePP) >= cfg.HintPP)
			{
				thePlayer.sendMessage("§2你的体力值(PP)一共" + (changePP >= 0 ? "增加" : "减少") + "了§6 " + df.format(Math.abs(changePP)) + " §c§l(" + df.format(pp) + "§f/§c§l" + df.format(maxPP) + ")");
				changePP = 0;
			}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof GameData)
			return playerName.equals(((GameData) obj).playerName);
		return false;
	}

	/**
	 * 处理中心
	 * **/
	public static class Handler
	{
		private static HashMap<String, GameData> nameMap = new HashMap<>();
		private static HashMap<Player, GameData> playerMap = new HashMap<>();
		private static Runnable ticker = new Runnable()
		{
			public void run()
			{
				tick();
			}
		};
		private static Runnable updater = new Runnable()
		{
			public void run()
			{
				update();
			}
		};

		public static void init(JavaPlugin plugin)
		{
			plugin.getServer().getScheduler().runTaskTimer(plugin, updater, 0, 20l);
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, ticker, 0, (long) CONFIG.cfg.Tick);
		}

		private static void tick()
		{
			Iterator<GameData> it = nameMap.values().iterator();
			GameData data;
			while (it.hasNext())
			{
				data = it.next();
				data.tick();
			}
		}

		private static void update()
		{
			Iterator<GameData> it = nameMap.values().iterator();
			GameData data;
			while (it.hasNext())
			{
				data = it.next();
				data.update();
			}
		}

		protected static void add(Player p)
		{
			GameData data = new GameData(p);
			playerMap.put(p, data = DataSaver.loadInto(data));
			nameMap.put(data.playerName.toLowerCase(), data);

			data.join();
			data.refreshHook();
		}

		protected static void remove(Player p)
		{
			GameData data = find(p);
			if (data != null)
				data.quit();

			if (find(p.getName()) != null)
				nameMap.remove(p.getName().toLowerCase());
			if (find(p) != null)
				playerMap.remove(p);
		}

		public static GameData find(String name)
		{
			return nameMap.get(name.toLowerCase());
		}

		public static GameData find(Player p)
		{
			return playerMap.get(p);
		}

		public static double getPP(String name)
		{
			GameData data = find(name);
			if (data != null)
				return data.getPP();
			if (DataHandler.exists(name))
			{
				return DataHandler.get(name).getDouble("PP");
			}
			return -1;
		}

		public static double getMaxPP(String name)
		{
			GameData data = find(name);
			if (data != null)
				return data.getMaxPP();
			if (DataHandler.exists(name))
			{
				return DataHandler.get(name).getDouble("MaxPP");
			}
			return -1;
		}
	}

}
