package project.kg;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import me.kg.fastuse.CTOS;
import me.kg.filedata.DataHandler;
import project.kg.hook.Hooks;
import project.kg.stamina.player.ActionListener;
import project.kg.stamina.player.DataSaver;
import project.kg.stamina.player.GameData;
import project.kg.stamina.set.CONFIG;
import project.kg.stamina.set.CONFIGsFans;
import project.kg.stamina.set.CmdPP;

public class STAMINA extends JavaPlugin
{
	public static final Permission perAdmin = new Permission("HyperStamina.admin");

	public static final boolean experiment = true;
	public static final String ver = "v1.1";

	@Override
	public void onEnable()
	{
		CTOS.setup(this);

		CTOS.Tick.begin();

		msg("����汾: " + (experiment ? "��cʵ��� " : "��ʽ�汾  ") + ver + " " + (experiment ? "��c(ʵ��汾������BUG��ǰ��MCBBS�ò�������ڽ��л�������л!)" : ""));
		msg("��ǰ�����汾Ϊ: " + this.getServer().getBukkitVersion());

		saveDefaultConfig();
		new CONFIG(getConfig());/*��ȡ����*/
		CONFIGsFans.callThem();/*��CONFIG��С����������һ��.*/

		DataHandler.init(this);/*��ʼ������ϵͳ*/

		GameData.Handler.init(this);/*��ʼ����Ҵ�����*/

		CTOS.register(new CmdPP(), "PP");
		CTOS.register(new ActionListener());/*ע���¼�*/

		DataSaver.init(this);/*���ݴ�����*/

		Hooks.hookAll(this);/*HOOK*/

		msg("��c�������(" + CTOS.Tick.end() + "ms)");
	}

	public static void msg(String msg)
	{
		CTOS.console.sendMessage("��7[��fSTAMINA��7]��e" + msg);
	}
}
