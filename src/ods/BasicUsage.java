package ods;

import com.github.miachm.sods.SpreadSheet;

import java.io.File;
import java.io.IOException;

public class BasicUsage
{	static final int START = 0; static final int END = 1;
	static final int[] DRUMDEFAULTS = {142,190}, DRUMNAMES = {125,140};

	public static void main(String[] args)
	{	try
		{	SpreadSheet spread = new SpreadSheet(new File("/home/thomas/Dokumente/Computer/XG/XGspec2_korr.ods"));
			System.out.println("Number of sheets: " + spread.getNumSheets());
			DrumNames.parse(spread.getSheets().subList(DRUMNAMES[START], DRUMNAMES[END]));
			DrumDefaults.parse(spread.getSheets().subList(DRUMDEFAULTS[START], DRUMDEFAULTS[END]));
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
}