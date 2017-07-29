package project.kg.hook;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import project.kg.STAMINA;

public class Hooks
{
	private static HashMap<Class<? extends IPluginHooker>, Object> hookerMap = new HashMap<>();

	public static void hookAll(JavaPlugin plugin)
	{
		hook(plugin, new SimplePlaceholderHook());
	}

	public static void hook(JavaPlugin plugin, IPluginHooker hooker)
	{
		String out = hooker.hook(plugin);
		if (!out.equalsIgnoreCase(""))
			STAMINA.msg(" > ¡ì2" + out);
		hookerMap.put(hooker.getClass(), hooker);
	}
}
