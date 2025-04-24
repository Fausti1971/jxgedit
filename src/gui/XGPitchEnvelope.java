package gui;
import java.awt.*;import java.awt.geom.GeneralPath;
import application.XGMath;
import module.XGModule;import value.XGFixedValue;
import value.XGValue;import xml.XMLNode;
import javax.swing.*;

public class XGPitchEnvelope extends JPanel implements XGComponent, XGShaper, XGValueComponent
{
	private static final XGFixedValue VALUE_255 = new XGFixedValue("fix", 255), VALUE_381 = new XGFixedValue("fix", 381);
	private static final String IL = "mp_peg_init_level", AT = "mp_peg_attack_time", RL = "mp_peg_release_level", RT ="mp_peg_release_time";

	static XGPitchEnvelope newPitchEnvelope(XGModule mod, XMLNode node)throws XGComponentException
	{	return new XGPitchEnvelope(mod.getValues().get(IL), mod.getValues().get(AT), mod.getValues().get(RL), mod.getValues().get(RT));
	}

/*********************************************************************************************************************/

	private final XGValue attackL, attackT, releaseL, releaseT;
	private final XGValue[] values;

	public XGPitchEnvelope(XGValue al, XGValue at, XGValue rl, XGValue rt)throws XGComponentException
	{	XGPointPanel panel;
		if(al == null) throw new XGComponentException("init level value is null");
		if(at == null) throw new XGComponentException("attack time value is null");
		if(rl == null) throw new XGComponentException("release level value is null");
		if(rt == null) throw new XGComponentException("release time value is null");
		this.attackT = at;
		this.attackL = al;
		this.releaseL = rl;
		this.releaseT = rt;
		this.values = new XGValue[]{this.attackL, this.attackT, this.releaseL, this.releaseT};

		this.setName("Pitch Envelope Generator");

		panel = new XGPointPanel(this, 1, 2, 0, 64, 0, 384, 0, 127, "Time", "Pitch");
		panel.addMouseListener(this);

		this.attackL.getValueListeners().add((XGValue v)->panel.repaint());
		this.attackT.getValueListeners().add((XGValue v)->panel.repaint());
		this.releaseL.getValueListeners().add((XGValue v)->panel.repaint());
		this.releaseT.getValueListeners().add((XGValue v)->panel.repaint());

		this.setLayout(new GridBagLayout());
		this.add(panel, DEF_GBC);
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();

		float midY = r.height / 2F;
		float width = r.width - r.x;
		float maxWidth = width / 3F;
		float minWidth = maxWidth / 10;

		float al = XGMath.linearScale(this.attackL.getValue(), 0, 127, r.height, r.y);
		float at = XGMath.linearScale(this.attackT.getValue(), 0, 127, minWidth, maxWidth);
		float rl = XGMath.linearScale(this.releaseL.getValue(), 0, 127, r.height, r.y);
		float rt = XGMath.linearScale(this.releaseT.getValue(), 0, 127, minWidth, maxWidth);

		gp.moveTo(r.x, midY);
		gp.lineTo(r.x, al);//ein moveTo ist für das Schließen des Pfades nicht relevant
		gp.quadTo(at/2.0, midY, at, midY);
		gp.lineTo(width - rt, midY);
		gp.quadTo(width - rt/2.0, midY, width, rl);
		gp.lineTo(width, midY);//ein moveTo ist für das Schließen des Pfades nicht relevant

		return gp;
	}

	@Override public XGValue[] getValues(){	return this.values;}
}
