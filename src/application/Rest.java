package application;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public interface Rest
{	static final String REGEX_ALPHA = "[a-zA-Z0-9_]+";
	static final String REGEX_NUMBER = "\\d+";

/**
 * zerlegt einen String an eventuell vorhandenen Kommas und returniert ein Set an (von führenden und folgenden Leerzeichen bereinigten) Strings;
 * @param s	Eingangsstring mit (oder ohne) Kommas
 * @return	Set der einzelnen Stringabschnitte
 */
	static Set<String> splitCSV(String s)
	{	Set<String> a = new HashSet<>();
		if(s != null)
		{	StringTokenizer t = new StringTokenizer(s, ",");
			while(t.hasMoreElements()) a.add(t.nextToken().trim());
		}
		return a;
	}

	static int linearIO(int i, int in_min, int in_max, int out_min, int out_max)
	{	float
			in = i - in_min,
			in_range = in_max - in_min,
			out_range = out_max - out_min;
		return(int)(in / in_range * out_range + out_min);
//		return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min)); original
	}

	static int parseIntOrDefault(String s, int def)
	{	try
		{	return Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{	return def;
		}
	}

	static String getStringOrDefault(String s, String def)
	{	if(s == null) return def;
		return s;
	}

	static boolean isNumber(String name)
	{	return name.matches(REGEX_NUMBER);
	}

	static boolean isAlpha(String name)
	{	return name.matches(REGEX_ALPHA);
	}

}
