package gui;

import java.awt.Graphics2D;
import javax.swing.JButton;
import adress.XGAddress;
import adress.XGMemberNotFoundException;
import application.XGLoggable;
import application.XGMath;
import module.XGModule;
import value.XGValue;
import value.XGValueChangeListener;
import xml.XMLNode;

public class XGEnvelopePoint extends JButton implements XGValueChangeListener, GuiConstants, XGLoggable
{
	private static final long serialVersionUID = 1L;

/*************************************************************************************************/

	private final XGEnvelope envelope;
	private final XGAddress addressX, addressY;
	private final XGValue valueX, valueY;
	private Graphics2D g2;

	public XGEnvelopePoint(XGEnvelope env, XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	//super(n, mod);
		this.envelope = env;

		this.addressX = new XGAddress(n.getStringAttribute(ATTR_ADDRESS_X), mod.getAddress());
		this.addressY = new XGAddress(n.getStringAttribute(ATTR_ADDRESS_Y), mod.getAddress());
		this.valueX = mod.getDevice().getValues().getFirstIncluded(this.addressX);
		this.valueY = mod.getDevice().getValues().getFirstIncluded(this.addressY);

		env.setOrigin(n.getIntegerAttribute(ATTR_ORIGIN, 0));
		env.setMinX(this.valueX.getParameter().getMinIndex());
		env.setMaxX(this.valueX.getParameter().getMaxIndex());
		env.setMinY(this.valueY.getParameter().getMinIndex());
		env.setMaxY(this.valueY.getParameter().getMaxIndex());

		env.setTableX(this.valueX.getParameter().getTranslationTable());
		env.setTableY(this.valueY.getParameter().getTranslationTable());

		this.valueX.addValueListener(this);
		this.valueY.addValueListener(this);

		LOG.info(this.getClass().getSimpleName() + " " + this.getName() + " initialized");
	}

//	@Override public void paint(Graphics g)
//		{	if(!(g instanceof Graphics2D)) return;
//			this.g2 = (Graphics2D)g.create();
//			this.g2.addRenderingHints(AALIAS);
//	// draw foreground
//			this.g2.setColor(COL_BAR_FORE);
//			this.g2.drawArc(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0, 360);
////			this.g2.fillRoundRect(this.loX, 0, this.hiX - this.loX, this.getHeight(), ROUND_RADIUS, ROUND_RADIUS);
//			this.g2.dispose();
//		}

	@Override public void contentChanged(XGValue v)
	{	String s = "X=" + this.valueX + "/" + "Y=" + this.valueY;
		int offs = GRID/2;
		this.setToolTipText(s);
		this.setName(s);
		int x = XGMath.linearIO(this.valueX.getValue(), this.envelope.getMinX(), this.envelope.getMaxX(), this.envelope.getX(), this.envelope.getWidth()) - offs;
		int y = XGMath.linearIO(this.valueY.getValue(), this.envelope.getMinY(), this.envelope.getMaxY(), this.envelope.getY(), this.envelope.getHeight()) - offs;
		this.setBounds(x, y, GRID, GRID);
		this.repaint();
	}

}
