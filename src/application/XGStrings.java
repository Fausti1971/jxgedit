package application;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public interface XGStrings
{
	static final String REGEX_ALNUM = "\\w+"; // alternativ: [a-zA-Z0-9_]+
	static final String REGEX_NON_ALNUM = "\\W+"; //alternativ: [^a-zA-Z0-9_]
	static final String REGEX_NUM = "\\d+";
	static final String REGEX_ALL = "[\\s\\S]";
	static final String TEXT_REPLACEMENT = "_";

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
	{	return name.matches(REGEX_NUM);
	}

	static boolean isAlNum(String name)
	{	return name.matches(REGEX_ALNUM);
	}
}
