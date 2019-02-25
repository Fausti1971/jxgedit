package parm;

import java.util.Map;
import java.util.function.Function;
import msg.XGMessageParameterChange;
import obj.XGObject;

public class XGParameter implements XGParameterConstants
{	private static final ByteType DEF_BYTE_TYPE = ByteType.MIDIBYTE;
	private static final Function<XGParameter, String> DEF_TRANS_TYPE = ValueTranslation::translateToText;
	private static final int DEF_MIN = 0, DEF_MAX = 127, DEF_SIZE = 1;

	/*****************************************************************************************************/

	public int min = DEF_MIN, max = DEF_MAX, offset, size = DEF_SIZE;
	public String longName, shortName;
	Function<XGParameter, String> valueTranslation;
	public Map<Integer, String> translationMap;
	private ByteType byteType;
	private final Tags tag;
	public XGObject obj;

	public XGParameter(Tags tag, XGObject o, int offset, int min, int max, String lName, String sName)
	{	this(tag, o, offset, DEF_SIZE, DEF_BYTE_TYPE, DEF_TRANS_TYPE, min, max, lName, sName);
	}

	public XGParameter(Tags tag, XGObject o, int offset, int size, ByteType bType, Map<Integer, String> table, int min, int max, String lName, String sName)
	{	this.tag = tag;
		this.obj = o;
		this.offset = offset;
		this.size = size;
		this.byteType = bType;
		this.min = min;
		this.max = max;
		this.longName = lName;
		this.shortName = sName;
		this.valueTranslation = ValueTranslation::translateMap;
		this.translationMap = table;
	}

	public XGParameter(Tags tag, XGObject o, int offset, int size, ByteType bType, Function<XGParameter, String> tType, int min, int max, String lName, String sName)
	{	this.tag = tag;
		this.obj = o;
		this.offset = offset;
		this.size = size;
		this.byteType = bType;
		this.min = min;
		this.max = max;
		this.longName = lName;
		this.shortName = sName;
		this.valueTranslation = tType;
	}

	public int getValue()
	{	if(obj == null) return 0;
		return getInt(this);
	}

	public boolean setValue(int v)
	{	if(obj == null) return false;
		int old = getValue();
		boolean changed = (old != setInt(this, Math.min(max, Math.max(min, v))));
		if(changed) new XGMessageParameterChange(this).transmit();
		else System.out.println("not changed: " + old + "/" + v);
		return changed;
	}

	public boolean addValue(int v)
	{	return setValue(getValue() + v);
	}

	public String getValueAsText()
	{	return valueTranslation.apply(this);
	}

	public Tags getTag()
	{	return this.tag;
	}

	public ByteType getByteType()
	{	return this.byteType;
	}

	public byte[] getByteArray()
	{	return obj.getByteArray();
	}
}
