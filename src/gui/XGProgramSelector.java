package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JButton;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import module.XGModule;
import msg.XGMessageBulkDump;
import msg.XGMessengerException;
import parm.XGParameter;
import parm.XGParameterChangeListener;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGProgramSelector extends XGComponent implements XGParameterChangeListener, XGValueChangeListener, ActionListener
{
	private static final long serialVersionUID = 1L;

/*********************************************************************************************/

	private final XGValue value;
	private final XGAddress address;
	private final JButton inc = new JButton(">"), dec = new JButton("<"), select;

	public XGProgramSelector(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{
		super(n, mod);
		this.setLayout(new BorderLayout());
		
		this.address = new XGAddress(n.getStringAttribute(ATTR_ADDRESS), mod.getAddress());
		this.value = mod.getDevice().getValues().getFirstIncluded(this.address);
		this.value.addParameterListener(this);
		this.value.addValueListener(this);
		this.setName(this.value.getParameter().getLongName());

		this.borderize();

		this.addMouseListener(this);
		this.addFocusListener(this);

		this.inc.addActionListener(this);
		this.add(this.inc, BorderLayout.EAST);

		this.dec.addActionListener(this);
		this.add(this.dec, BorderLayout.WEST);

		this.select = new JButton(this.value.toString());
		this.select.addActionListener(this);
		this.add(this.select, BorderLayout.CENTER);

		this.parameterChanged(this.value.getParameter());
	}

	@Override public void contentChanged(XGValue v)
	{
int i = v.getValue();
System.out.println("content: msb=" + ((i >> 14) & 0x7F) + " lsb=" + ((i >> 7) & 0x7F) + " prg=" + (i & 0x7F));
		this.select.setText(this.value.toString());
		this.repaint();
	}

	@Override public void parameterChanged(XGParameter p)
	{
		this.repaint();
	}

	@Override public void actionPerformed(ActionEvent e)
	{	boolean changed = false;
		if(e.getSource() == inc) changed = this.value.setIndex(this.value.getIndex() + 1);
		if(e.getSource() == dec) changed = this.value.setIndex(this.value.getIndex() - 1);
		if(e.getSource() == select) System.out.println("select");
		if(changed)
		{	XGAddress adr = this.value.getAddress();
			try
			{	
				XGMessageBulkDump msg = new XGMessageBulkDump(this.value.getSource(), this.value.getSource().getDevice().getMidi(), new XGAddress(adr.getHi(), adr.getMid(), this.value.getOpcode().getAddress().getLo()));
				msg.encodeLSB(msg.getBaseOffset(), msg.getBulkSize(), this.value.getValue());
				msg.setChecksum();
				msg.transmit();
			}
			catch(InvalidXGAddressException|InvalidMidiDataException | XGMessengerException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
//		this.value.transmit();
	}
}
