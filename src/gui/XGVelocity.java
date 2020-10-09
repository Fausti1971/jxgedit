package gui;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JPanel;
import adress.XGAddress;
import device.XGDevice;
import module.XGModule;
import parm.XGParameter;
import value.XGFixedValue;
import value.XGValue;
import xml.XMLNode;

public class XGVelocity extends XGComponent
{
	private static final long serialVersionUID = 1L;

/***************************************************************************************/

	private final JPanel panel;
	private final XGAbsolutePoint startPoint, endPoint;
	private final XGValue valStart, valEnd, valDepth, valOffset;
	private XGParameter parStart, parEnd, parDepth, parOffset;
	private int iMin, iMax;
	private Graphics2D g2;
	private Insets ins;
	private Rectangle area = new Rectangle();

	public XGVelocity(XMLNode n, XGModule mod)
	{	super(n, mod);
		XGDevice dev = mod.getType().getDevice();
		XGAddress adrStart, adrEnd, adrDepth, adrOffset;
		adrStart = new XGAddress(n.getStringAttribute(ATTR_START_X), mod.getAddress());
		adrEnd = new XGAddress(n.getStringAttribute(ATTR_END_X), mod.getAddress());
		adrDepth = new XGAddress(n.getStringAttribute(ATTR_DEPTH), mod.getAddress());
		adrOffset = new XGAddress(n.getStringAttribute(ATTR_OFFSET), mod.getAddress());
		this.valStart = dev.getValues().getOrDefault(adrStart, DEF_VALUE);
		this.valEnd = dev.getValues().getOrDefault(adrEnd, DEF_VALUE);
		this.valDepth = dev.getValues().getOrDefault(adrDepth, DEF_VALUE);
		this.valOffset = dev.getValues().getOrDefault(adrOffset, DEF_VALUE);

		this.borderize();
		this.setLayout(null);
		this.panel = new JPanel();
		this.startPoint = new XGAbsolutePoint(this.panel, this.valStart, new XGFixedValue("fix", 0));
		this.endPoint = new XGAbsolutePoint(this.panel, this.valEnd, new XGFixedValue("fix", 0));

		this.add(this.panel);
		this.panel.addPoint(this.startPoint);
		this.panel.addPoint(this.endPoint);
	}
}
