package project.kg.stamina.player;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.collect.Sets;

import me.kg.fastuse.CTOS;
import project.kg.stamina.player.behavior.BehaviorType;
import project.kg.stamina.player.behavior.MotionType;
import project.kg.stamina.set.CONFIG;

public class ActionListener implements Listener
{
	private static ArrayList<UUID> cooldown = new ArrayList<>();

	public static CONFIG cfg;

	public ActionListener()
	{
		Bukkit.getScheduler().runTaskTimerAsynchronously(CTOS.getPlugin(), new Runnable()
		{

			@Override
			public void run()
			{
				cooldown.clear();
			}
		}, 0, 80L);
	}

	/**For GameData**/
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		GameData.Handler.add(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		GameData.Handler.remove(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void pluginDisable(PluginDisableEvent event)
	{
		DataSaver.saveTasks();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			GameData.Handler.remove(p);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPluginEnable(PluginEnableEvent event)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			GameData.Handler.add(p);
		}
	}

	/**For Game**/
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if (cfg.RespawnRefection)
		{
			GameData data = GameData.Handler.find(event.getPlayer());
			if (data == null)
				return;
			data.setPP(data.maxPP);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPostBlockBreak(BlockBreakEvent event)
	{
		if (event.isCancelled())
			return;

		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		if (data.limited(BehaviorType.breakblock))
		{
			event.setCancelled(true);
			warm(event.getPlayer());
			return;
		}
		data.decreasePP(cfg.defBreakBlockValue);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onChat(AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
			return;
		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		if (data.limited(BehaviorType.chat))
		{
			event.setCancelled(true);
			warm(event.getPlayer());
			return;
		}
		data.decreasePP(cfg.Chat);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onDeath(EntityDamageByEntityEvent event)
	{
		if (event.isCancelled())
			return;
		if (!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof LivingEntity))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(((LivingEntity) event.getEntity()).getHealth() - event.getDamage() <= 0))
			return;

		Player damager = (Player) event.getDamager();
		GameData data = GameData.Handler.find(damager);
		if (data == null)
			return;
		if (damager.getGameMode().equals(GameMode.CREATIVE))
			return;
		data.decreasePP(cfg.Kill);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onCmd(PlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled())
			return;
		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		if (data.limited(BehaviorType.cmd))
		{
			event.setCancelled(true);
			warm(event.getPlayer());
			return;
		}
		data.decreasePP(cfg.DoCmd);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onDropItem(PlayerDropItemEvent event)
	{
		if (event.isCancelled())
			return;
		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		if (data.limited(BehaviorType.dropitem))
		{
			event.setCancelled(true);
			warm(event.getPlayer());
			return;
		}
		data.decreasePP(cfg.DropItem);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled())
			return;
		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		if (data.limited(BehaviorType.placeblock))
		{
			event.setCancelled(true);
			warm(event.getPlayer());
			return;
		}
		data.decreasePP(cfg.defPlaceBlockValue);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onSprint(PlayerToggleSprintEvent event)
	{
		if (event.isCancelled())
			return;
		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		if (data.limited(BehaviorType.sprint))
		{
			event.setCancelled(true);
			warm(event.getPlayer());
			return;
		}
	}

	private Set<UUID> prevPlayersOnGround = Sets.newHashSet();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onJump(PlayerMoveEvent e)
	{
		if (e.isCancelled())
			return;

		Player player = e.getPlayer();

		GameData data = GameData.Handler.find(e.getPlayer());
		if (data == null)
			return;

		if (player.getGameMode().equals(GameMode.CREATIVE))
			return;

		if (player.getVelocity().getY() > 0)
		{
			double jumpVelocity = (double) 0.42F;
			if (player.hasPotionEffect(PotionEffectType.JUMP))
			{
				PotionEffect eff = null;
				for (PotionEffect reff : player.getActivePotionEffects())
				{
					if (reff.getType().equals(PotionEffectType.JUMP))
						eff = reff;
				}
				jumpVelocity += (double) ((float) (eff.getAmplifier() + 1) * 0.1F);
			}
			if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId()))
			{
				if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0)
				{
					if (data.limited(BehaviorType.jump))
						player.setVelocity(new Vector(0, -1, 0));
					else
						data.decreasePP(cfg.Jump);
				}
			}
		}
		if (player.isOnGround())
		{
			prevPlayersOnGround.add(player.getUniqueId());
		} else
		{
			prevPlayersOnGround.remove(player.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if (event.isCancelled())
			return;

		GameData data = GameData.Handler.find(event.getPlayer());
		if (data == null)
			return;

		if (!event.getPlayer().isOnGround())
			return;

		Player p = event.getPlayer();

		double dis = event.getFrom().distance(event.getTo());
		if (dis < 0.01)
			return;
		MotionType type;
		if (p.isSprinting())
			type = MotionType.sprint;
		else
			type = MotionType.walk;
		data.motionHandler.moved(type, dis);
	}

	private static void warm(Player p)
	{
		if (cooldown.contains(p.getUniqueId()))
			return;
		p.sendMessage("§c你的体力值不足.");
		cooldown.add(p.getUniqueId());
	}
}
