package project.kg.stamina.set;

import project.kg.stamina.player.ActionListener;
import project.kg.stamina.player.GameData;
import project.kg.stamina.player.behavior.MotionCalculator;

/**
 * �������ʵ���岢�����������
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
