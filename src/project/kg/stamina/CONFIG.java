package project.kg.stamina;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kg.fastuse.AutoLoad;
import me.kg.fastuse.FastConfig;
import project.kg.STAMINA;
import project.kg.stamina.player.behavior.BehaviorType;

public class CONFIG extends FastConfig
{
	public static CONFIG cfg;

	@AutoLoad
	public final Integer DataSaveInterval = 100, Tick = 10;
	@AutoLoad
	public final Double DefaultMaxPP = 100.0, TickRefection = 0.1, HintPP = 5.0;
	@AutoLoad
	public final Boolean HookExpBar = false, RespawnRefection = false, RecoverWhenStill = false, SaveData = true;

	public final Boolean Hint;
	//ValueConsume.BreakBlock
	@AutoLoad(path = "ValueConsume.", target = "BreakBlock")
	public final Double defBreakBlockValue = 1.0;
	@AutoLoad(path = "ValueConsume.", target = "PlaceBlock")
	public final Double defPlaceBlockValue = 0.2;

	@AutoLoad(path = "ValueConsume.")
	public final Double Sprint = 0.5, Walk = 0.25, Chat = 0.0, Kill = 0.5, DoCmd = 0.0, DropItem = 0.1, Jump = 0.3;

	@AutoLoad
	public final Double WeakThreshold = 1.0, ExhaustedThreshold = 0.2;

	public ArrayList<PotionEffect> weakBuffs = new ArrayList<>(), exhaustedBuffs = new ArrayList<>();;
	public ArrayList<BehaviorType> weakLimit = new ArrayList<>(), exhaustedLimit = new ArrayList<>();

	public CONFIG(FileConfiguration config)
	{
		super("", config);

		setup(this);
		cfg = this;

		Hint = HintPP != -1;

		reflectEffs("WeakBuff", "weak");
		reflectEffs("ExhaustedBuff", "exhausted");
		STAMINA.msg("更新速度(TICK)已设置为: §c§l" + Tick);
	}

	private void reflectEffs(String pathName, String listName)
	{
		ArrayList<BehaviorType> types = new ArrayList<>();
		ArrayList<PotionEffect> potions = new ArrayList<>();

		for (String str : config.getStringList(pathName))
		{
			try
			{
				if (str.contains(":"))
				{
					String limit = str.replaceFirst("\\:", "");
					BehaviorType type = BehaviorType.find(limit);
					if (type == null)
					{
						STAMINA.msg("§e未找到限制效果 " + str);
						return;
					} else
					{
						types.add(type);
					}
				} else
				{
					String[] args = str.split("\\s+");
					PotionEffectType type = PotionEffectType.getByName(args[0]);
					PotionEffect eff = new PotionEffect(type, 25, Integer.parseInt(args[1]));
					potions.add(eff);
				}
			} catch (Exception e)
			{
				STAMINA.msg("§c在加载 " + pathName + "." + str + " 时发生错误,请检查设置.");
			}

		}

		try
		{
			Field fli = CONFIG.class.getField(listName + "Limit");
			Field fbuf = CONFIG.class.getField(listName + "Buffs");

			fli.setAccessible(true);
			fli.set(this, types);

			fbuf.setAccessible(true);
			fbuf.set(this, potions);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
