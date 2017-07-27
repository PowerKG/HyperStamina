package me.kg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class FileTool
{

	public static List<String> readTxtFile(String filePath)
	{
		File file = new File(filePath);
		if (file.isFile() && file.exists())
		{
			return readTxtFile(file);
		}
		return null;
	}

	public static List<String> readTxtFile(File file)
	{
		List<String> returned = new ArrayList<>();

		try
		{
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				returned.add(line);
			}
			br.close();
			return returned;
		} catch (Exception e)
		{
			return null;
		}
	}

}
