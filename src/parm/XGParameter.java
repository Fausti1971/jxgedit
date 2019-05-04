package parm;

import java.util.Map;
import application.InvalidXGAdressException;
import msg.Bytes;
import obj.XGAdress;
import obj.XGObject;

public class XGParameter implements XGParameterConstants
{	

/*****************************************************************************************************/

	private final ParameterType type;
	private final int mutableMasterOffset, mutableMapIndex;
	private final XGParameterOpcode opcode;
	private final XGParameterDescription description;

	public XGParameter(int offs)	//automatischer Konstruktor - unbekannter Parameter
	{	this(new XGParameterOpcode(offs), new XGParameterDescription(offs, 0, 0, "automatic generated unknown parameter", "offs=" + offs, "translateToText", null, null));}

	public XGParameter(XGParameterDescription desc)
	{	this.type = ParameterType.SECONDARY;
		this.opcode = null;
		this.description = desc;
		this.mutableMasterOffset = 0;
		this.mutableMapIndex = 0;
	}

	public XGParameter(XGParameterOpcode opc, XGParameterDescription desc)
	{	this.type = ParameterType.COMPLETE;
		this.description = desc;
		this.opcode = opc;
		this.mutableMasterOffset = 0;
		this.mutableMapIndex = 0;
	}

	public XGParameter(XGParameterOpcode opc, int masterOffset, int mapIndex)
	{	this.type = ParameterType.PRIMARY;
		this.description = null;
		this.opcode = opc;
		this.mutableMasterOffset = masterOffset;
		this.mutableMapIndex = mapIndex;
	}

	public XGParameterDescription getDescription(XGValue v)
	{	if(this.description != null || v == null) return this.description;
		XGValue masterValue;
		try
		{	masterValue = XGObject.getValue(new XGAdress(v.getAdress().getHi(), v.getAdress().getMid(), this.getMasterOffset()));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();
			return null;
		}
		return ParameterMap.getParameterMap(masterValue.getTranslatedValue()).get(this.mutableMapIndex).description;
	}

	public ParameterType getType()
	{	return type;}

	public int getMasterOffset()
	{	return this.mutableMasterOffset;}

	public int getMutableIndex()
	{	return this.mutableMapIndex;}

	public int getOffset()
	{	return this.opcode.getOffset();}

	public Bytes.ByteType getByteType()
	{	return this.opcode.getByteType();}

	public int getByteCount()
	{	return this.opcode.getByteCount();}

	public String getLongName()
	{	return this.getDescription(null).getLongName();}

	public String getShortName()
	{	return this.description.getShortName();}

	public Map<Integer, String> getTranslationMap()
	{	return this.description.getTranslationMap();}

	public ValueType getValueType()
	{	return this.opcode.getValueType();}

	public int getMaxValue()
	{	return this.description.getMaxValue();}

	public int getMinValue()
	{	return this.description.getMinValue();}

	public int limitize(int v)
	{	return Math.max(getMinValue(), Math.min(getMaxValue(), v));}

	public String translate(XGValue v)
	{	return this.description.getValueTranslator().translate(v);}

	@Override public String toString()
	{	return this.getLongName();}
}
