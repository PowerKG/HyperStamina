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
					sender.sendMessage("��c���� /pp help(?) �鿴����ָ��.");
					return false;
				} else
				{
					GameData data = GameData.Handler.find(p);
					sender.sendMessage("��2��Ŀǰ������ֵ(PP)Ϊ: ��c��l" + data.getPP() + "/" + data.getMaxPP());
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("info"))
			{
				ArrayList<String> cmds = new ArrayList<>();
				cmds.add("��c��lHyper Stamina @Coded by KG");
				cmds.add("��e����汾: " + (STAMINA.experiment ? "��cʵ��� " : "��ʽ�汾  ") + STAMINA.ver + " " + (STAMINA.experiment ? "��c(ʵ��汾������BUG��ǰ��MCBBS�ò�������ڽ��л�������л!)" : ""));
				for (String str : cmds)
					sender.sendMessage(str);
				return true;
			}
			if (args[0].equalsIgnoreCase("help"))
			{
				ArrayList<String> cmds = new ArrayList<>();
				cmds.add("��9��l�����ǿ���ָ��:");
				cmds.add("��c> /pp -��ѯPPֵ.");
				if (sender.isOp() || sender.hasPermission(STAMINA.perAdmin))
				{
					cmds.add("��c> /pp (���) -��ѯ���PPֵ.");
					cmds.add("��c> /pp set (���) (PP) -�������PPֵ.");
					cmds.add("��c> /pp give (���) (PP) -�������PPֵ.");
					cmds.add("��c> /pp take (���) (PP) -�۳����PPֵ.");
				}
				cmds.add("��6> /pp info -�˽�����Ϣ.");
				for (String str : cmds)
					sender.sendMessage(str);
				return true;
			}
			if (args.length == 1)
			{
				if (!(sender.isOp() || sender.hasPermission(STAMINA.perAdmin)))
				{
					sender.sendMessage("��c��û��Ȩ����ô��.");
					return false;
				}
				if (args.length != 1)
				{
					sender.sendMessage("��c��������,ʹ�� /pp help(?) �鿴��ȷָ��.");
					return false;
				}
				double pp = GameData.Handler.getPP(args[0]);
				double maxPP = GameData.Handler.getMaxPP(args[0]);
				if (pp == -1)
				{
					sender.sendMessage("��c����Ҳ�����.");
					return false;
				}
				sender.sendMessage("��2" + args[0] + "������ֵ(PP)Ϊ: ��c��l" + pp + "/" + maxPP);
				return true;
			}
			if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take"))
			{
				if (!(sender.isOp() || sender.hasPermission(STAMINA.perAdmin)))
				{
					sender.sendMessage("��c��û��Ȩ����ô��.");
					return false;
				}
				if (args.length != 3)
				{
					sender.sendMessage("��c��������,ʹ�� /pp help(?) �鿴��ȷָ��.");
					return false;
				}
				GameData data = GameData.Handler.find(args[1]);
				if (data == null)
				{
					sender.sendMessage("��6����Ҳ�����.");
					return false;
				}
				double amount = -1;
				try
				{
					amount = Double.parseDouble(args[2]);
				} catch (Exception e)
				{
					sender.sendMessage("��c�����ֵ��������");
					return false;
				}
				if (amount < 0)
				{
					sender.sendMessage("��cPPֵ������С��0.");
					return false;
				}
				String done = "";

				if (args[0].equalsIgnoreCase("set"))
				{
					data.setPP(amount);
					done = "����Ϊ";
				} else if (args[0].equalsIgnoreCase("give"))
				{
					data.increasePP(amount);
					done = "����";
				} else if (args[0].equalsIgnoreCase("take"))
				{
					data.decreasePP(amount);
					done = "�۳�";
				}
				sender.sendMessage("��6�ɹ������ ��l" + data.playerName + " ��l��PP" + done + " ��c��l" + amount);
				return false;
			}
		}
		return false;
	}

}
