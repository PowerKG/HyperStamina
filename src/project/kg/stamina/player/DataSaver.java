package project.kg.stamina.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.kg.filedata.DataHandler;
import project.kg.stamina.set.CONFIG;

public class DataSaver
{
	private static boolean save = true;
	public static final String pathMaxPP = "MaxPP", pathPP = "PP", pathLastSave = "LastSave";

	protected static GameData loadInto(GameData data)
	{
		for (GameData d : needToSave)
		{
			if (d.playerName.toLowerCase().equals(data.playerName.toLowerCase()))
				return d;
		}
		if (DataHandler.exists(data.playerName))
		{
			FileConfiguration config = DataHandler.get(data.playerName);
			data.maxPP = checkNull(config.getDouble(pathMaxPP), CONFIG.cfg.DefaultMaxPP);
			data.pp = checkNull(config.getDouble(pathPP), data.maxPP);
		} else
		{
			data.maxPP = CONFIG.cfg.DefaultMaxPP;
			data.pp = data.maxPP;
		}
		return data;
	}

	private static ArrayList<GameData> needToSave = new ArrayList<>();
	private static ArrayList<GameData> cache = new ArrayList<>();
	private static TaskTimer timer = null;

	public static void init(JavaPlugin plugin)
	{
		save = CONFIG.cfg.SaveData;
		if (timer == null && save)
			plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, timer = new TaskTimer(), 0L, (long) CONFIG.cfg.DataSaveInterval);
	}

	public synchronized static void addTask(GameData data)
	{
		if (save)
			if (!needToSave.contains(data))
				needToSave.add(data);
	}

	public synchronized static void saveTasks()
	{
		Date date = new Date();

		cache.addAll(needToSave);
		needToSave.clear();

		ArrayList<GameData> clone = new ArrayList<>();
		clone.addAll(cache);

		Iterator<GameData> it = clone.iterator();

		GameData data;
		while (it.hasNext())
		{
			data = it.next();

			FileConfiguration config;

			if (DataHandler.exists(data.playerName))
				config = DataHandler.get(data.playerName);
			else
				config = DataHandler.createPlayerFile(data.playerName);

			config.set(pathLastSave, date.getTime());
			config.set(pathMaxPP, data.maxPP);
			config.set(pathPP, data.pp);

			try
			{
				DataHandler.save(config, data.playerName);
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			data.isSaving = false;
			cache.remove(data);
		}
	}

	private static class TaskTimer implements Runnable
	{
		public void run()
		{
			saveTasks();
		}
	}

	public static <T> T checkNull(T obj, T nullReturn)
	{
		return obj == null ? nullReturn : obj;
	}

}
