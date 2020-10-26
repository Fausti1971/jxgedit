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

/**
 * 	returniert möglichst den Integerwert des übergebenen Strings, im Fehlerfall den Defaultwert
 * @param s	zu interpretierender String
 * @param def Fallbackwert
 * @return Integer
 */
	static int parseIntOrDefault(String s, int def)
	{	try
		{	return Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{	return def;
		}
	}

/**
 * versucht aus den im übergebenen String, durch "." getrennten Strings durch einen Bitshift um 7 Bits nach links einen Integerwert zu interpretieren
 * @param s String mit durch "." getrennten 7Bit MSBs
 * @param def Fallbackwert
 * @return Integer
 */
	static int parseValue(String s, int def)
	{	if(s == null) return def;
		int i = 0;
		for(String n : s.split("\\."))
		{	i <<= 7;
			i |= XGStrings.parseIntOrDefault(n, def);
		}
		return i;
	}

/**
 * überprüft den übergebenen String lediglich auf null
 * @param s	übergebener String, kann null sein
 * @param def	FallbackString, falls s null ist
 * @return String s oder def
 */
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

	static String toShortName(String name)//TODO: das kannst Du besser!
	{	return name.substring(0, Math.min(name.length(), 4));
	}
}
