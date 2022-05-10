package application;

import static application.XGLoggable.LOG;import java.awt.*;import java.util.*;

public interface XGStrings
{
	String REGEX_ALNUM = "\\w+"; // alternativ: [a-zA-Z0-9_]+
	String REGEX_NON_ALNUM = "\\W+"; //alternativ: [^a-zA-Z0-9_]
	String REGEX_NUM = "\\d+";
	String REGEX_ALL = "[\\s\\S]";
	String TEXT_REPLACEMENT = "_";
	String[] KEY = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};

	static String toAlNum(String dirty)
	{	return dirty.replaceAll(REGEX_NON_ALNUM, TEXT_REPLACEMENT);
	}

/**
 * zerlegt einen String an eventuell vorhandenen Kommas und returniert ein Set an (von führenden und folgenden Leerzeichen bereinigten) Strings;
 * @param s	Eingangsstring mit (oder ohne) Kommas
 * @return	Set der einzelnen Stringabschnitte
 */
	static LinkedHashSet<String> splitCSV(String s)
	{	LinkedHashSet<String> a = new LinkedHashSet<>();
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

	static boolean isNumber(String name){	return name.matches(REGEX_NUM);}

	static boolean isAlNum(String name){	return name.matches(REGEX_ALNUM);}

	static String commonString(String s1, String s2)
	{	int i = 0, last = Math.min(s1.length(), s2.length());
		for(; i < last; i++) if(s1.charAt(i) != s2.charAt(i)) break;
		return s1.substring(0, i);
	}

	static void main(String[] args)
	{	System.out.println("String:");
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		toRectangle(s);
	}

	static Rectangle toRectangle(String s)
	{	Rectangle r = new Rectangle();
		String[] n = s.split(",");
		if(n.length != 4) throw new RuntimeException(s + " contains " + n.length + " tokens! (4 tokens expected)");
		for(String c : n) if(!XGStrings.isNumber(c)) throw new RuntimeException("token " + c + " of string " + s + " is not a number!"); 
		r.x = Integer.parseInt(n[0]);
		r.y = Integer.parseInt(n[1]);
		r.width = Integer.parseInt(n[2]);
		r.height = Integer.parseInt(n[3]);
		return r;
	}

	//static Rectangle toGrid(String s)
	//{	Rectangle r = new Rectangle();
	//	s = s.toUpperCase().trim();
	//	String[] n = s.split("[A-Z]");
	//	String[] a = s.split("\\d+");
	//	int i = 0;
	//	Point[] p = new Point[]{new Point(),new Point()};
	//	for(String c : a)
	//		if(XGStrings.isAlNum(c))
	//			p[i++].x = c.charAt(0) - 'A';
	//	i = 0;
	//	for(String c : n)
	//		if(XGStrings.isNumber(c))
	//			p[i++].y = Integer.parseInt(c);
	//
	//	r.x = Math.min(p[0].x, p[1].x);
	//	r.y = Math.min(p[0].y, p[1].y);
	//	r.width = abs(p[0].x - p[1].x) + 1;
	//	r.height = abs(p[0].y - p[1].y) + 1;
	//	return r;
	//}
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
	static String toShortName(String name, int length)
	{	if(name.length() <= length) return name;
		String[] words = name.split(" |\\/");
		int i = 0;
		for(String s : words) words[i++] = s.replaceAll("a|e|i|o|u", "");
		int lettersPerWord = length / words.length;
		StringBuilder res = new StringBuilder();
		for(String s : words){	res.append(s,0, Math.min(s.length(), lettersPerWord));}
		return String.valueOf(res);
	}

	static String toHexString(byte[] array)
	{	if(array == null) return "no data";
		String separator = ", ";
		StringBuilder s = new StringBuilder();
		for(byte c : array)
		{	s.append(String.format("%02X", c & 0xFF));
			s.append(separator);
		}
		return s.deleteCharAt(s.lastIndexOf(separator)).toString();
	}

	static byte[] fromHexString(String content) throws NumberFormatException
	{	String[] sa =  content.split(",");
		byte[] array = new byte[sa.length];
		int i = 0;
		for(String s : sa) array[i++] = (byte)(Integer.parseInt(s, 16));
		return array;
	}

	static int toNumber(String s)
	{	if(s == null) throw new NumberFormatException("Argument is null");
		return Integer.parseInt(s.replaceAll("\\D", ""));
	}

	static int decodeKey(String key)//TODO: falsch
	{	String oct = key.replaceAll("[A-G,a-g]#?", "");
		String note = key.replaceAll("-?[0-8]", "");

		int o = (Integer.parseInt(oct) + 2) * 12;
		for(int k = 0; k < KEY.length; k++) if(KEY[k].equalsIgnoreCase(note)) return o + k;
		return o;
	}

	static String encodeKey(int key){	return KEY[key % 12] + (key/12 - 2);}
	}
