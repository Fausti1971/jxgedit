package application;

import static application.XGLoggable.LOG;import static java.lang.Math.abs;import java.awt.*;import java.util.HashSet;
import java.util.Scanner;import java.util.Set;
import java.util.StringTokenizer;

public interface XGStrings
{
	static final String REGEX_ALNUM = "\\w+"; // alternativ: [a-zA-Z0-9_]+
	static final String REGEX_NON_ALNUM = "\\W+"; //alternativ: [^a-zA-Z0-9_]
	static final String REGEX_NUM = "\\d+";
	static final String REGEX_ALL = "[\\s\\S]";
	static final String TEXT_REPLACEMENT = "_";

	static String toAlNum(String dirty)
	{	return dirty.replaceAll(REGEX_NON_ALNUM, TEXT_REPLACEMENT);
	}

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

	static String valueToString(int v)
	{	StringBuilder res = new StringBuilder();
		while(true)
		{	res.insert(0, v & 127);
			v >>= 7;
			if(v != 0) res.insert(0, ".");
			else break;
		}
		return res.toString();
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

	static String commonString(String s1, String s2)
	{	int i = 0, last = Math.min(s1.length(), s2.length());
		for(; i < last; i++)
		{	if(s1.charAt(i) != s2.charAt(i)) break;
		}
		return s1.substring(0, i);
	}

	static void main(String[] args)
	{	System.out.println("String:");
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		toGrid(s);
	}

	static Rectangle toGrid(String s)
	{	Rectangle r = new Rectangle();
		s = s.toUpperCase().trim();
		String[] n = s.split("[A-Z]");
		String[] a = s.split("\\d+");
		int i = 0;
		Point[] p = new Point[]{new Point(),new Point()};
		for(String c : a)
			if(XGStrings.isAlNum(c))
				p[i++].x = c.charAt(0) - 'A';
		i = 0;
		for(String c : n)
			if(XGStrings.isNumber(c))
				p[i++].y = Integer.parseInt(c);
		r.x = Math.min(p[0].x, p[1].x);
		r.y = Math.min(p[0].y, p[1].y);
		r.width = abs(p[0].x - p[1].x) + 1;
		r.height = abs(p[0].y - p[1].y) + 1;
		return r;
	}
/*
	static Rectangle toGrid(String s)
	{	Rectangle r = new Rectangle(0, 0,0 ,0);
		if(s != null)
		{	String[] sec = s.split(",");
			if(sec.length == 4)
			{	int x1 = Integer.parseInt(sec[0]);
				int y1 = Integer.parseInt(sec[1]);
				int x2 = Integer.parseInt(sec[2]);
				int y2 = Integer.parseInt(sec[3]);
				r.x = Math.min(x1, x2);
				r.y = Math.min(y1, y2);
				r.width = abs(x1 - x2) + 1;
				r.height = abs(y1 - y2) + 1;
			}
			else LOG.severe("string (" + s + ") contains no valid grid...");
		}
		return r;
	}
*/
	static String toShortName(String name)//TODO: das kannst Du besser!
	{	return name.substring(0, Math.min(name.length(), 4));
	}

	static String toHexString(byte[] array)
	{	if(array == null) return "no data";
		StringBuilder s = new StringBuilder();
		for(byte c : array)
		{	s.append(String.format("%02X", c & 0xFF));
			s.append(", ");
		}
		return s.toString();
	}

	static int toNumber(String s)
	{	return Integer.parseInt(s.replaceAll("\\D", ""));
	}

	static String toAlpha(String s)
	{	return s.replaceAll("[-+]\\d", "");
	}
}
