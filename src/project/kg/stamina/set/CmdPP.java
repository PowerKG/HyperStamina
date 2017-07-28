package project.kg.stamina.set;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import project.kg.STAMINA;
import project.kg.stamina.player.GameData;

public class CmdPP implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = null;
		if (sender instanceof Player)
			p = (Player) sender;

		if (label.equalsIgnoreCase("pp"))
		{
			if (args.length == 0)
			{
				if (p == null)
				{
					sender.sendMessage("§c输入 /pp help(?) 查看更多指令.");
					return false;
				} else
				{
					GameData data = GameData.Handler.find(p);
					sender.sendMessage("§2你目前的体力值(PP)为: §c§l" + data.getPP() + "/" + data.getMaxPP());
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("info"))
			{
				ArrayList<String> cmds = new ArrayList<>();
				cmds.add("§c§lHyper Stamina @Coded by KG");
				cmds.add("§e插件版本: " + (STAMINA.experiment ? "§c实验版 " : "正式版本  ") + STAMINA.ver + " " + (STAMINA.experiment ? "§c(实验版本内遇到BUG可前往MCBBS该插件帖子内进行回馈。感谢!)" : ""));
				for (String str : cmds)
					sender.sendMessage(str);
				return true;
			}
			if (args[0].equalsIgnoreCase("help"))
			{
				ArrayList<String> cmds = new ArrayList<>();
				cmds.add("§9§l以下是可用指令:");
				cmds.add("§c> /pp -查询PP值.");
				if (sender.isOp() || sender.hasPermission(STAMINA.perAdmin))
				{
					cmds.add("§c> /pp (玩家) -查询玩家PP值.");
					cmds.add("§c> /pp set (玩家) (PP) -设置玩家PP值.");
					cmds.add("§c> /pp give (玩家) (PP) -给予玩家PP值.");
					cmds.add("§c> /pp take (玩家) (PP) -扣除玩家PP值.");
				}
				cmds.add("§6> /pp info -了解插件信息.");
				for (String str : cmds)
					sender.sendMessage(str);
				return true;
			}
			if (args.length == 1)
			{
				if (!(sender.isOp() || sender.hasPermission(STAMINA.perAdmin)))
				{
					sender.sendMessage("§c你没有权限这么做.");
					return false;
				}
				if (args.length != 1)
				{
					sender.sendMessage("§c输入有误,使用 /pp help(?) 查看正确指令.");
					return false;
				}
				double pp = GameData.Handler.getPP(args[0]);
				double maxPP = GameData.Handler.getMaxPP(args[0]);
				if (pp == -1)
				{
					sender.sendMessage("§c该玩家不存在.");
					return false;
				}
				sender.sendMessage("§2" + args[0] + "的体力值(PP)为: §c§l" + pp + "/" + maxPP);
				return true;
			}
			if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take"))
			{
				if (!(sender.isOp() || sender.hasPermission(STAMINA.perAdmin)))
				{
					sender.sendMessage("§c你没有权限这么做.");
					return false;
				}
				if (args.length != 3)
				{
					sender.sendMessage("§c输入有误,使用 /pp help(?) 查看正确指令.");
					return false;
				}
				GameData data = GameData.Handler.find(args[1]);
				if (data == null)
				{
					sender.sendMessage("§6该玩家不存在.");
					return false;
				}
				double amount = -1;
				try
				{
					amount = Double.parseDouble(args[2]);
				} catch (Exception e)
				{
					sender.sendMessage("§c你的数值输入有误");
					return false;
				}
				if (amount < 0)
				{
					sender.sendMessage("§cPP值不可以小于0.");
					return false;
				}
				String done = "";

				if (args[0].equalsIgnoreCase("set"))
				{
					data.setPP(amount);
					done = "设置为";
				} else if (args[0].equalsIgnoreCase("give"))
				{
					data.increasePP(amount);
					done = "增加";
				} else if (args[0].equalsIgnoreCase("take"))
				{
					data.decreasePP(amount);
					done = "扣除";
				}
				sender.sendMessage("§6成功将玩家 §l" + data.playerName + " §l的PP" + done + " §c§l" + amount);
				return false;
			}
		}
		return false;
	}

}
