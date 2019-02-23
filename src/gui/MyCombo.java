package gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map.Entry;
import javax.swing.JComboBox;
import obj.XGObject;
import parm.XGParameter;

public class MyCombo extends JComboBox<Entry<Integer, String>> implements GuiConstants, XGObjectChangeListener, ItemListener
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	XGParameter parm;

	public MyCombo(XGParameter p)
	{	for(Entry<Integer, String> i : p.translationMap.entrySet())
		{	addItem(i);
		}
		this.parm = p;
//		setRenderer(aRenderer);
		setToolTipText(p.longName);
		setSize(SL_DIM);
		setMaximumRowCount(32);
		addItemListener(this);
	}

	public void objectChanged(XGObject o)
	{	parm.bind(o);
		getModel().setSelectedItem(parm.translationMap.get(parm.getValue()));
		repaint();
	}

	public void itemStateChanged(ItemEvent e)
	{	try
		{	parm.setValue(this.getItemAt(getSelectedIndex()).getKey());
		}
		catch(NullPointerException ex)
		{
		}
		
/*		Object o = this.getSelectedItem();
		if(o instanceof Entry)
		{	Entry<Integer,String> n = (Entry<Integer, String>)o;
			parm.setValue(n.getKey());
		}
*/	}
}
