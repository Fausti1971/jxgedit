package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import obj.XGObject;
import parm.XGParameter;
import parm.XGParameterConstants;

public class MyCombo extends JButton implements GuiConstants, XGObjectChangeListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	XGParameter parm;
	final Tags tag;

	public MyCombo(Tags tag)
	{	this.tag = tag;
		MyCombo mc = this;
		setSize(SL_DIM);
		addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	new MyPopup(mc).show(mc, 0, 0);
			}
		});
	}

	public void objectChanged(XGObject o)
	{	bind(o.parameters.get(this.tag));
	}

	private void bind(XGParameter p)
	{	if(p != null)
		{	this.parm = p;
			setToolTipText(p.longName);
			setVisible(true);
			setText(p.getValueAsText());
			repaint();
		}
	}

	public byte[] getByteArray()
	{	return parm.obj.getByteArray();
	}

	public void valueChanged(int v)
	{	this.parm.setValue(v);
		this.setText(this.parm.getValueAsText());
	}
}
