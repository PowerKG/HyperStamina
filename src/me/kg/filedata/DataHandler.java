package me.kg.filedata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class DataHandler
{

	private static File DatasPath = null;
	private static HashMap<String, Object> customDef = new HashMap<>();

	public static void addDefData(String path, Object obj)
	{
		customDef.put(path, obj);
	}

	public static boolean init(Plugin plugin)
	{
		/* ��ʼ��·�� */
		if (DatasPath == null)
			DatasPath = new File(plugin.getDataFolder() + "/userdata");

		/* ��ʼ���ļ��� */
		if (!DatasPath.exists())
			DatasPath.mkdir();

		return true;
	}

	/**
	 * �������ļ����Ƿ����
	 * 
	 * @param fileName
	 *            �ļ�(�������)
	 **/
	public static boolean exists(String fileName)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (f.exists())
			return true;
		return false;
	}

	/**
	 * �������Ĭ���ļ���
	 * 
	 * @param fileName
	 *            �ļ�(�������)
	 * 
	 * @return ���ظ����Ĭ���ļ��е�config
	 * 
	 * @exception �׳�д���ļ�����
	 **/

	public static FileConfiguration createPlayerFile(String fileName)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		FileConfiguration fileConfig = null;
		try
		{
			if (!f.exists())
				f.createNewFile();
			fileConfig = YamlConfiguration.loadConfiguration(f);

			/* д���ʼ���� */
			for (String s : customDef.keySet())
			{
				fileConfig.set(s, customDef.get(s));
			}

			fileConfig.save(f);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileConfig;
	}

	/**
	 * ��������ļ���
	 * 
	 * @param saveFileConfigurtion
	 *            ��Ҫ������ļ���
	 * @param fileName
	 *            �������
	 **/
	public static void save(FileConfiguration saveFileConfigurtion, String fileName) throws IOException
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		saveFileConfigurtion.save(f);
	}

	/**
	 * ���ļ���·��д��һ������(ͬʱ�������ļ������ھʹ���)
	 * 
	 * @param fileName
	 *            �ļ�·������(file.getName())
	 * @param path
	 *            ·��
	 * @param contents
	 *            д�������
	 * @throws PlayerFileNotFoundException
	 *             �Ҳ�����ҵ��ļ���
	 * 
	 * @exception �����ļ�����
	 **/
	public static void writeTo(String fileName, String path, Object contents) throws IOException, PlayerFileNotFoundException
	{
		FileConfiguration c = get(fileName);
		c.set(path, contents);
		save(c, fileName);
	}

	/**
	 * ��ȡ����ļ��ڵ���Ŀ
	 * 
	 * @param needToGet
	 *            ��Ҫ��ȡ���������
	 * @param path
	 *            ��ȡ��·��
	 * @throws PlayerFileNotFoundException
	 *             �Ҳ�����ҵ��ļ���
	 * 
	 **/
	public static Object getObject(String needToGet, String path) throws PlayerFileNotFoundException
	{

		FileConfiguration f = get(needToGet.toLowerCase());
		if (f != null)
			return f.get(path);

		return null;
	}

	public static File getFile(String fileName)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		return f;
	}

	/**
	 * ��ȡ���config
	 * 
	 * @param fileName
	 *            ��Ҫ��ȡ���������
	 * @throws PlayerFileNotFoundException
	 *             �Ҳ�����ҵ��ļ���
	 * 
	 **/
	public static FileConfiguration get(String fileName) throws PlayerFileNotFoundException
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (f.exists())
			return YamlConfiguration.loadConfiguration(f);
		throw new PlayerFileNotFoundException(f);
	}

	public static Object getProperty(String fileName, String Property)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (f.exists())
			return YamlConfiguration.loadConfiguration(f).get("Property." + Property);
		return null;
	}

	public static boolean setProperty(String fileName, String Property, Object obj)
	{
		File f = new File(DatasPath, fileName.toLowerCase() + ".yml");
		if (!f.exists())
		{
			createPlayerFile(fileName.toLowerCase());
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		config.set("Property." + Property, obj);

		try
		{
			config.save(f);
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ��ȡ���д��ڵ����config
	 **/
	public static File[] getExistsPlayerFile()
	{
		return DatasPath.listFiles();
	}

	/**
	 * ��ȡ����ļ�·��
	 **/
	public static File getPathFile()
	{
		return DatasPath;
	}
}
