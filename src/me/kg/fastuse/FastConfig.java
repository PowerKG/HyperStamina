package me.kg.fastuse;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.FileConfiguration;

public class FastConfig
{
	private String head;
	public final FileConfiguration config;

	public FastConfig(String head, FileConfiguration config)
	{
		this.head = head;
		this.config = config;
	}

	protected void setup(Object obj)
	{
		OhUseTheFuckingDarkReflect(head, obj, config);
	}

	private static void OhUseTheFuckingDarkReflect(String head, Object obj, FileConfiguration config)
	{
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field f : fields)
		{

			if (!(Modifier.isFinal(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && f.isAnnotationPresent(AutoLoad.class)))
			{
				continue;
			}

			f.setAccessible(true);

			AutoLoad autoload = f.getAnnotation(AutoLoad.class);

			String path = head + autoload.path();
			String target = autoload.target().equals("") ? f.getName() : autoload.target();

			if (f.getType().equals(Boolean.class))
			{
				try
				{
					f.set(obj, config.getBoolean(path + target));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
			if (f.getType().equals(Integer.class))
			{
				try
				{
					f.set(obj, config.getInt(path + target));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
			if (f.getType().equals(String.class))
			{
				try
				{
					if (config.getString(path + target) != null)
						f.set(obj, config.getString(path + target));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{
				}
			}
			if (f.getType().equals(Double.class))
			{
				try
				{
					f.set(obj, config.getDouble(path + target));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
			if (f.getType().equals(Long.class))
			{
				try
				{
					f.set(obj, config.getLong(path + target));
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (NullPointerException e)
				{

				}
			}
		}
	}
}
