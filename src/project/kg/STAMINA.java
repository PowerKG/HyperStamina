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

		msg("插件版本: " + (experiment ? "§c实验版 " : "正式版本  ") + ver + " " + (experiment ? "§c(实验版本内遇到BUG可前往MCBBS该插件帖子内进行回馈。感谢!)" : ""));
		msg("当前环境版本为: " + this.getServer().getBukkitVersion());

		saveDefaultConfig();
		new CONFIG(getConfig());/*读取数据*/
		CONFIGsFans.callThem();/*给CONFIG的小迷妹们人手一个.*/

		DataHandler.init(this);/*初始化数据系统*/

		GameData.Handler.init(this);/*初始化玩家处理器*/

		CTOS.register(new CmdPP(), "PP");
		CTOS.register(new ActionListener());/*注册事件*/

		DataSaver.init(this);/*数据处理器*/

		Hooks.hookAll(this);/*HOOK*/

		msg("§c加载完毕(" + CTOS.Tick.end() + "ms)");
	}

	public static void msg(String msg)
	{
		CTOS.console.sendMessage("§7[§fSTAMINA§7]§e" + msg);
	}
}
