package me.kg.fastuse;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Preconditions;

public class CTOS
{
	private static JavaPlugin plugin = null;
	public static ConsoleCommandSender console = null;

	public static void setup(JavaPlugin thePlugin)
	{
		if (plugin != null)
			purge();

		plugin = thePlugin;
		console = thePlugin.getServer().getConsoleSender();
	}

	public static JavaPlugin getPlugin()
	{
		return plugin;
	}

	public static void register(Listener listener)
	{
		checkNull(listener);
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	public static void register(CommandExecutor executor, String cmd)
	{
		checkNull(executor);
		plugin.getCommand(cmd).setExecutor(executor);
	}

	private static void checkNull(Object... objs)
	{
		Preconditions.checkNotNull(plugin, "Plugin is null");

		for (Object obj : objs)
			Preconditions.checkNotNull(obj, obj.toString() + " is null");
	}

	private static void purge()
	{
		plugin = null;
		console = null;
	}

	public static class Tick
	{
		private static Long a = null;

		public static void begin()
		{
			a = System.currentTimeMillis();
		}

		public static int end()
		{
			if (a != null)
			{
				int c = (int) (System.currentTimeMillis() - a);
				a = null;
				return c;
			} else
				return -1;
		}
	}

}
