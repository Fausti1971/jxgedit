package application;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public interface Rest
{	static Set<String> splitString(String s)
	{	if(s == null) return null;
		StringTokenizer t = new StringTokenizer(s, ",");
		Set<String> a = new HashSet<>();
		while(t.hasMoreElements()) a.add(t.nextToken());
		return a;
	}

	static int linearIO(int i, int in_min, int in_max, int out_min, int out_max)
	{	return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min));}

	static int parseIntOrDefault(String s, int def)
	{	try
		{	return Integer.parseInt(s);}
		catch(NumberFormatException e)
		{	return def;}
	}
}
