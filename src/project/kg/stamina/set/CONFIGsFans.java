package project.kg.stamina.set;

import project.kg.stamina.player.ActionListener;
import project.kg.stamina.player.GameData;
import project.kg.stamina.player.behavior.MotionCalculator;

/**
 * 这个类其实意义并不大哈哈哈哈
 * **/
public class CONFIGsFans
{
	public static void callThem()
	{
		MotionCalculator.cfg = CONFIG.cfg;
		ActionListener.cfg = CONFIG.cfg;
		GameData.cfg = CONFIG.cfg;
	}
}
