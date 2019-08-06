package adress;

import obj.XGObjectInstance;
import value.XGValue;
import value.XGValueChangeListener;

public interface XGInstanceSelectionListener extends XGValueChangeListener, XGAdressable
{	
	public default void instanceSelected(XGObjectInstance i)
	{	try
		{	this.valueChanged(XGValue.getValue(this.getAdress().complement(i.getAdress())));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}
}
