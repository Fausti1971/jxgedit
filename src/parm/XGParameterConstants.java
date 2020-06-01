package parm;

import java.util.function.Function;
import xml.XMLNodeConstants;

public interface XGParameterConstants extends XMLNodeConstants
{
	static final String
		TABLE_NORMAL = "normal",
		TABLE_ADD1 = "add1",
		TABLE_DIV10 = "div10",
		TABLE_SUB64 = "sub64",
		TABLE_SUB128DIV10 = "sub128div10",
		TABLE_SUB1024DIV10 = "sub1024div10",
		DEF_TABLENAME = TABLE_NORMAL;

	static final XGTable DEF_TABLE = new XGVirtualTable(
			DEF_TABLENAME,
			new Function<Integer, String>()
			{	@Override public String apply(Integer t)
				{	return t.toString();
				};
			},
			new Function<String, Integer>()
			{	@Override public Integer apply(String s)
				{	return Integer.parseInt(s);
				}
			});

	public static final XGParameter DUMMY_PARAMETER = new XGParameter("n/a", 0);
	static final String DEF_PARAMETERNAME = "unknown parameter: ";
	static final int  DEF_MIN = 0, DEF_MAX = 127;

	static enum ValueDataType
	{	MSB, LSB, MSN, LSN
	}

	static final ValueDataType DEF_DATATYPE = ValueDataType.LSB;
}
