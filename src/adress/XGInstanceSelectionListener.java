package adress;

import value.XGValue;
import value.XGValueChangeListener;

public interface XGInstanceSelectionListener extends XGValueChangeListener, XGAdressable
{	
	public default void instanceSelected(XGAdress adr)
	{	try
		{	this.valueChanged(XGValue.getValue(this.getAdress().complement(adr)));}
		catch(InvalidXGAdressException e)
		{	e.printStackTrace();}
	}
}
