package application;

import java.awt.*;import java.util.Arrays;public interface XGMath
{
	static Rectangle toRectangle(int[] array)
	{	if(array.length != 4) throw new RuntimeException("array " + Arrays.toString(array) + " has " + array.length + " values (4 values expected)");
		return new Rectangle(array[0], array[1], array[2], array[3]);
	}

	static int linearScale(int i, int in_min, int in_max, int out_min, int out_max)
	{	float
			in = i - in_min,
			in_range = in_max - in_min,
			out_range = out_max - out_min;
		return(int)(in / in_range * out_range + out_min);
//		return((int)(((float)(i - in_min) / (float)(in_max - in_min) * (out_max - out_min)) + out_min));// original
	}

	static float linearScale(int i, int in_min, int in_max, float out_min, float out_max)
	{	float
			in = i - in_min,
			in_range = in_max - in_min,
			out_range = out_max - out_min;
		return in / in_range * out_range + out_min;
	}

	static int linearScale(double i, double in_min, double in_max, int out_min, int out_max)
	{	double
			in = i - in_min,
			in_range = in_max - in_min,
			out_range = out_max - out_min;
		return Math.round(Math.round(in / in_range * out_range + out_min));
	}
//Test
	static void createTable(String[] args)
	{	int min = 0, max = 139, i;
		float f = 0f;
		for(i = min; i <= max; i++)
		{	f = linearScale(i, min, max, 0.1f, 36.2f);
			System.out.println("<item value=\"" + i + "\" name=\"" + String.format("%1$4.2f", f) + "\"/>");
		}
	}
}
