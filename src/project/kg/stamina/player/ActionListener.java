package project.kg.stamina.player;

import org.bukkit.Bukkit;
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

import project.kg.stamina.CONFIG;
import project.kg.stamina.player.behavior.BehaviorType;
import project.kg.stamina.player.behavior.MotionType;

public class ActionListener implements Listener
{
	public static CONFIG cfg;

	public ActionListener()
	{
		cfg = CONFIG.cfg;
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

		if (data.limited(BehaviorType.breakblock))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c你的体力值不足!无法破坏方块!");
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
		if (data.limited(BehaviorType.chat))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c你的体力值不足!无法讲话!");
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
		if (data.limited(BehaviorType.cmd))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c你的体力值不足!无法发送命令!");
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
		if (data.limited(BehaviorType.dropitem))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c你的体力值不足!无法丢物品!");
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
		if (data.limited(BehaviorType.placeblock))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c你的体力值不足!无法放置方块!");
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
		if (data.limited(BehaviorType.sprint))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c你的体力值不足!无法冲刺!");
			return;
		}
	}

	@SuppressWarnings("deprecation")
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
}
