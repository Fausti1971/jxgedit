package gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import obj.XGObject;
import parm.XGParameter;
import parm.XGParameterConstants;

public class MyCombo extends JComboBox<Entry<Integer, String>> implements GuiConstants, XGObjectChangeListener, ItemListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	XGParameter parm;
	final Tags tag;
//	Vector<Entry<Integer, String>> model;

	public MyCombo(Tags tag)
	{	this.tag = tag;
//		setRenderer(aRenderer);
		setSize(SL_DIM);
		setVisible(false);
		setMaximumRowCount(32);
		addItemListener(this);
	}

	public void objectChanged(XGObject o)
	{	bind(o.parameters.get(this.tag));
		getModel().setSelectedItem(parm.translationMap.get(parm.getValue()));
		repaint();
	}

	private void bind(XGParameter p)
	{	if(p != null)
		{	this.parm = p;
			setToolTipText(p.longName);
//			setModel(new MyComboModel(this.parm.translationMap));
			setVisible(true);
//			removeAllItems();
			for(Entry<Integer, String> i : this.parm.translationMap.entrySet())
			{	addItem(i);
			}
		}
	}

	public void itemStateChanged(ItemEvent e)
	{	try
		{	parm.setValue(this.getItemAt(getSelectedIndex()).getKey());
		}
		catch(NullPointerException ex)
		{
		}
		
	}

	public byte[] getByteArray()
	{	return parm.obj.getByteArray();
	}
/*
	class MyComboModel implements ComboBoxModel<Entry<Integer, String>>
	{	Map<Integer, String> map;
		Entry<Integer, String> selected;

		public MyComboModel(Map<Integer, String> m)
		{	this.map = m;
		}

		public int getSize()
		{	return this.map.size();
		}

		public Entry<Integer,String> getElementAt(int index)
		{	Set<Entry<Integer, String>> s = map.entrySet();
			return s.toArray(new Entry[getSize()])[index];
		}

		public void addListDataListener(ListDataListener l)
		{
		}

		public void removeListDataListener(ListDataListener l)
		{
		}

		public void setSelectedItem(Object anItem)
		{	for(Entry<Integer, String> e : this.map.entrySet())
			{	if(e.equals(anItem)) selected = e;
			}
		}

		public Object getSelectedItem()
		{	return selected;
		}
	}
*/}
