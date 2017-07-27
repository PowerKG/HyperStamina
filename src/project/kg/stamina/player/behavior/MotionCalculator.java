package project.kg.stamina.player.behavior;

import java.util.HashMap;

import me.kg.fastclass.Box;
import project.kg.stamina.CONFIG;

public class MotionCalculator
{
	public static CONFIG cfg;

	private HashMap<MotionType, Box<Double>> distanceMap = new HashMap<>();

	public void moved(MotionType type, double disance)
	{
		if (!distanceMap.containsKey(type))
			distanceMap.put(type, new Box<Double>(0.0));
		distanceMap.get(type).obj += disance;
	}

	public double get(MotionType type)
	{
		if (!distanceMap.containsKey(type))
			return 0.0;
		return distanceMap.get(type).obj;
	}

	public double calculate()
	{
		double cost = 0;
		for (MotionType type : distanceMap.keySet())
		{
			double con;
			switch (type)
			{
			case walk:
				con = cfg.Walk;
				break;
			case sprint:
				con = cfg.Sprint;
				break;
			default:
				con = 0.0;
				break;
			}
			cost += distanceMap.get(type).obj * con;
			distanceMap.get(type).obj = 0.0;
		}
		return cost;
	}
}