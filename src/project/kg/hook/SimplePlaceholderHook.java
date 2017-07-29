package project.kg.hook;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import project.kg.stamina.player.GameData;

public class SimplePlaceholderHook implements IPluginHooker
{
	private static class hook extends EZPlaceholderHook
	{
		public hook(Plugin plugin, String identifier)
		{
			super(plugin, identifier);
		}

		@Override
		public String onPlaceholderRequest(Player p, String str)
		{
			if (p == null)
				return "";
			GameData data = GameData.Handler.find(p);
			if (data == null)
				return "";
			if (str.equalsIgnoreCase("now"))
			{
				return String.valueOf(data.getPP());
			} else if (str.equalsIgnoreCase("now_f"))
			{
				return GameData.df.format(data.getPP());
			} else if (str.equalsIgnoreCase("now_i"))
			{
				return String.valueOf((int) data.getPP());
			} else if (str.equalsIgnoreCase("max"))
			{
				return String.valueOf(data.getMaxPP());
			}
			return null;
		}
	}

	public String hook(JavaPlugin plugin)
	{
		if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
			try
			{
				new hook(plugin, "pp").hook();
			} catch (Exception err)
			{
				return "§c连接PlaceholderAPI失败.";
			}
			return "连接PlaceholderAPI成功,papi变量已可用.";
		}
		return "";
	}

}
