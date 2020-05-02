package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JTextField;
import adress.InvalidXGAddressException;
import device.XGDevice;
import msg.XGMessageParameterChange;
import value.XGValue;
import value.XGValueChangeListener;

public class XGValueLabel extends JTextField implements GuiConstants, ActionListener, XGValueChangeListener, MouseListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************/

	private final XGValue value;

	public XGValueLabel(XGValue v)
	{	super();
		this.value = v;
		this.setBackground(COL_TRANSPARENT);
		this.setBorder(null);
		this.setText(this.value.toString());
		this.setFont(FONT);
		this.setHorizontalAlignment(JTextField.CENTER);
		this.value.addListener(this);
		this.addMouseListener(this);
		this.addActionListener(this);
	}

	@Override public void actionPerformed(ActionEvent e)
	{
		XGDevice dev = this.value.getSource().getDevice();
		boolean changed = this.value.setContent(this.value.getParameter().getValueTranslator().translate(this.value, this.getText().trim()));
		if(changed)
		{	try
			{	new XGMessageParameterChange(dev, dev.getMidi(), this.value).transmit();
			}
			catch(InvalidXGAddressException | InvalidMidiDataException e1)
			{	e1.printStackTrace();
			}
		}
		this.setText(this.value.toString());
		this.repaint();
	}

	@Override public void contentChanged(XGValue v)
	{	this.setText(this.value.toString());
		this.repaint();
	}

	@Override public void mouseClicked(MouseEvent e)
	{	this.selectAll();
	}

	@Override public void mousePressed(MouseEvent e)
	{
	}

	@Override public void mouseReleased(MouseEvent e)
	{
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}
}
