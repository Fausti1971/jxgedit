package application;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public interface Rest
{	static Set<String> splitStringByComma(String s)
	{	Set<String> a = new HashSet<>();
		if(s != null)
		{	StringTokenizer t = new StringTokenizer(s, ",");
			while(t.hasMoreElements()) a.add(t.nextToken().trim());
		}
		return a;
	}

	static int linearIO(int i, int in_min, int in_max, int out_min, int out_max)
	{	return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min));
	}

	static int parseIntOrDefault(String s, int def)
	{	try
		{	return Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{	return def;
		}
	}

	static String splitStringByUnderscore(String s)
	{	return s.substring(s.lastIndexOf("_")).trim();
	}

	static String getStringOrDefault(String s, String def)
	{	if(s == null) return def;
		return s;
	}
}
