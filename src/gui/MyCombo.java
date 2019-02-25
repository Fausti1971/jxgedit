package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import obj.XGObject;
import parm.Opcode;
import parm.XGParameter;
import parm.XGParameterConstants;

public class MyCombo extends JButton implements GuiConstants, XGObjectSelectionListener, XGParameterConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	private XGParameter parm;
	private Opcode opcode;

	public MyCombo(Opcode opc)
	{	this.opcode = opc;
		MyCombo mc = this;
		setSize(SL_DIM);
		setVisible(false);
		addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	new MyPopup(mc).show(mc, 0, 0);
			}
		});
	}

	public XGParameter getParm()
	{	return this.parm;
	}

	public void setParm(XGParameter parm)
	{	this.parm = parm;
	}

	public void xgObjectSelected(XGObject o)
	{	bind(o.getParameter(this.opcode.getOffset()));
	}

	private void bind(XGParameter p)
	{	if(p != null)
		{	this.setParm(p);
			setToolTipText(p.getLongName());
			setVisible(true);
			setText(p.getValueAsText());
			repaint();
		}
	}

	public void valueChanged(int v)
	{	this.getParm().setValue(v);
		this.setText(this.getParm().getValueAsText());
	}
}
