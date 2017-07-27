package project.kg.stamina.player.behavior;

import java.util.HashMap;

public enum BehaviorType
{
	dropitem, cmd, chat, sprint, breakblock, placeblock;

	private static HashMap<String, BehaviorType> typeMap = new HashMap<>();
	static
	{
		for (BehaviorType type : values())
		{
			typeMap.put(type.name().toLowerCase(), type);
		}
	}

	public static BehaviorType find(String str)
	{
		return typeMap.get(str.toLowerCase());
	}
}
