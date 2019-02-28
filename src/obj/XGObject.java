package obj;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import application.ChangeListener;
import msg.XGMessageParameterChange;
import parm.XGParameter;
import parm.XGParameterConstants;

public abstract class XGObject implements XGObjectConstants, XGParameterConstants
{	protected static final Logger log = Logger.getAnonymousLogger();

	protected static ChangeListener listener = null;

/******************** Instance ********************************************************************************************/

	protected final XGAdress adress;
	protected final Map<Integer, Integer> values = new HashMap<>();

	protected XGObject(XGAdress adr)
	{	this.adress = adr;}

	public int getValue(int offset)
	{	return this.values.getOrDefault(offset, 0);
	}

	
	public void setValue(int offset, int v)
	{	this.values.put(offset, v);
	}

	public boolean changeValue(int offset, int v)
	{	v = getParameter(offset).limit(v);
		boolean changed = this.getValue(offset) != v;
		this.setValue(offset, v);
		if(changed) new XGMessageParameterChange(this, this.getParameter(offset)).transmit();
		return changed;
	}

	public boolean addValue(int offset, int v)
	{	return changeValue(offset, getValue(offset) + v);}

	public XGAdress getAdr()
	{	return adress;}

/************* Overrides ***********************************************************************************************************/

/*********** abstract *******************************************************************************************************************/

	public abstract XGParameter getParameter(int offset);
}
