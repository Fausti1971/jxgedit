package gui;

import java.awt.*;
import java.awt.geom.*;
import static application.XGLoggable.LOG;import application.XGMath;import static gui.XGUI.DEF_GBC;
import module.XGModule;
import tag.*;
import value.XGFixedValue;
import value.XGValue;
import value.XGValueChangeListener;import javax.swing.*;

/*
Q = 0.1 - 12.0 (1...120); 1 / Q = B / F => 
B = F / Q; B = Bandbreite
N = log(1 + 1 / (2 × Q2) + sqr(((2 + 1 / (Q2))2) / 4 − 1)) / log(2); N = B in Oktaven
G = -12dB - +12dB (52...76); 1 tick/dB
F = 20Hz - 20kHz (0...60); table3 10 octaves (6 ticks/1octave)
Beachte: überlagerte Gains werden addiert!

Hinweis: Alle Berechnungen gelten nur für Bandbreiten am -3 dB Punkt. Ist der Pegeländerung geringer als 3 dB, wird das tatsächliche Ergebnis davon abweichen. 
Diese Eigenart ist jedoch für den Praxisgebrauch wenig relevant. 
Ebenfalls darf die Bandbreite oder Q nicht mit der Slope eines (Shelving) Filters verwechselt werden,
auch wenn eine andere Bandbreite eine andere Steigung der Bearbeitungskurve veranlasst.
 */

public class XGMEQ extends JPanel implements XGShaper, XGValueChangeListener
{
	private static final int SHELV = 0, PEAK = 1;
	private static final XGFixedValue PEAK_VALUE = new XGFixedValue("shape", PEAK);
	private static final int F_MIN = 0, F_MAX = 60, G_MIN = 52, G_MAX = 76, Q_MIN = 1, Q_MAX = 120;

	public static final String
		G1 = "eq_gain1",
		G2 = "eq_gain2",
		G3 = "eq_gain3",
		G4 = "eq_gain4",
		G5 = "eq_gain5",
		F1 = "eq_freq1",
		F2 = "eq_freq2",
		F3 = "eq_freq3",
		F4 = "eq_freq4",
		F5 = "eq_freq5",
		Q1 = "eq_q1",
		Q2 = "eq_q2",
		Q3 = "eq_q3",
		Q4 = "eq_q4",
		Q5 = "eq_q5",
		S1 = "eq_shape1",
		S5 = "eq_shape5";

/*****************************************************************************************/

	private final XGPointPanel panel;
	private final XGFreqBand[] bands = new XGFreqBand[5];

	public XGMEQ(XGModule mod)throws XGComponentException
	{
		try
		{	XGTagableAddressableSet<XGValue> set = mod.getValues();
			XGValue g1 = set.get(G1);
			XGValue g2 = set.get(G2);
			XGValue g3 = set.get(G3);
			XGValue g4 = set.get(G4);
			XGValue g5 = set.get(G5);

			XGValue f1 = set.get(F1);
			XGValue f2 = set.get(F2);
			XGValue f3 = set.get(F3);
			XGValue f4 = set.get(F4);
			XGValue f5 = set.get(F5);

			XGValue q1 = set.get(Q1);
			XGValue q2 = set.get(Q2);
			XGValue q3 = set.get(Q3);
			XGValue q4 = set.get(Q4);
			XGValue q5 = set.get(Q5);

			XGValue s1 = set.get(S1);
			XGValue s5 = set.get(S5);

			this.bands[0] = (new XGFreqBand(f1, g1, q1, s1));
			this.bands[1] = (new XGFreqBand(f2, g2, q2, PEAK_VALUE));
			this.bands[2] = (new XGFreqBand(f3, g3, q3, PEAK_VALUE));
			this.bands[3] = (new XGFreqBand(f4, g4, q4, PEAK_VALUE));
			this.bands[4] = (new XGFreqBand(f5, g5, q5, s5));

			this.panel = new XGPointPanel(this, 1, 9, 0, 0, F_MIN, F_MAX, G_MIN, G_MAX, "Octaves", "Gain");
			this.panel.setName("");

			g1.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			g2.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			g3.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			g4.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			g5.getValueListeners().add((XGValue v)->{this.panel.repaint();});

			f1.getValueListeners().add(this);
			f2.getValueListeners().add(this);
			f3.getValueListeners().add(this);
			f4.getValueListeners().add(this);
			f5.getValueListeners().add(this);

			q1.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			q2.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			q3.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			q4.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			q5.getValueListeners().add((XGValue v)->{this.panel.repaint();});

			s1.getValueListeners().add((XGValue v)->{this.panel.repaint();});
			s5.getValueListeners().add((XGValue v)->{this.panel.repaint();});

		this.setLayout(new GridBagLayout());
		this.add(this.panel, DEF_GBC);

		//float q = 0;
		//for(int i = Q_MIN; i <= Q_MAX; i++)
		//{	q = ((float)i)/10;
		//	System.out.println("q=" + q + "; oct=" + (float)(Math.log(1+1/(2*q*q)+Math.sqrt((Math.pow(2+1.0/(q*q),2))/4-1))/Math.log(2)));
		//}

		}
		catch(NullPointerException e)
		{	throw new XGComponentException(e.getMessage());
		}
	}

	public void contentChanged(XGValue v){	this.panel.repaint();}

	public Shape getShape(Rectangle r)//alle 5 Kurven nacheinander, geschnitten und addiert; prinzipielle richtig aber sieht scheiße aus...
	{	GeneralPath gp = new GeneralPath();
		float[] sum = new float[61];
		float midY = r.height / 2F;

		for(XGFreqBand b : this.bands)
		{	gp.reset();
			float f = XGMath.linearScale(b.frequency.getValue(), F_MIN, F_MAX, r.x, r.width);
			float startY = midY;
			float endY = midY;
			float q = XGMath.linearScale(60f/b.q.getValue()/2, F_MIN, F_MAX, r.x, r.width);
			float fl = f - q/2;
			float fu = f + q/2;
			float g = midY + XGMath.linearScale(b.gain.getValue(), G_MIN, G_MAX, midY, -midY);
			if(b.shape.getValue() == SHELV && S1.equals(b.shape.getTag())) startY = g;
			if(b.shape.getValue() == SHELV && S5.equals(b.shape.getTag())) endY = g;

	//		https://youtu.be/D2cgE3exNTI

			gp.moveTo(r.x, r.height);
			gp.lineTo(r.x, startY);
			gp.lineTo(fl - q, startY);
			gp.curveTo(fl-q/2, startY, fl, g, f, g);
			gp.curveTo(fu, g, fu+q/2, endY, fu+q, endY);
			gp.lineTo(r.width, endY);
			gp.lineTo(r.width, r.height);

			Area a1 = new Area(gp);
			Area a2;
			for(int i = F_MIN; i <= F_MAX; i++)
			{	float x = XGMath.linearScale(i, F_MIN, F_MAX, r.x, r.width);
				Rectangle2D.Float r2 = new Rectangle2D.Float(Math.min(r.width - 1, x), r.y, 1, r.height);
				a2 = new Area(r2);
				if(a1.intersects(r2))
				{	a2.intersect(a1);
					sum[i] += a2.getBounds2D().getY();
				}
			}
		}
		gp.reset();
		gp.moveTo(r.x, midY);
		CubicCurve2D.Float curve = new CubicCurve2D.Float();
		for(int i = F_MIN; i <= F_MAX; i++)
		{	float x = XGMath.linearScale(i, F_MIN, F_MAX, r.x, r.width);
			gp.lineTo(x, sum[i] / 5);
		}
		gp.lineTo(r.width, midY);
		return gp;
	}

	//public Shape getShape(Rectangle r)//alle 5 Kurven übereinander
	//{	GeneralPath gp = new GeneralPath();
	//	float midY = r.height / 2F;
	//
	//	for(XGFreqBand b : this.bands)
	//	{	float f = XGMath.linearScale(b.frequency.getValue(), F_MIN, F_MAX, r.x, r.width);
	//		float startY = midY;
	//		float endY = midY;
	//		float q = XGMath.linearScale(60f/b.q.getValue()/2, F_MIN, F_MAX, r.x, r.width);
	//		float fl = f - q/2;
	//		float fu = f + q/2;
	//		float g = midY + XGMath.linearScale(b.gain.getValue(), G_MIN, G_MAX, midY, -midY);
	//		if(b.shape.getValue() == SHELV && S1.equals(b.shape.getTag())) startY = g;
	//		if(b.shape.getValue() == SHELV && S5.equals(b.shape.getTag())) endY = g;
	//		gp.moveTo(r.x, startY);
	//
	//		gp.lineTo(fl - q, startY);
	//		gp.curveTo(fl-q/2, startY, fl, g, f, g);
	//		gp.curveTo(fu, g, fu+q/2, endY, fu+q, endY);
	//		gp.lineTo(r.width, endY);
	//		gp.lineTo(r.width, midY);
	//		gp.lineTo(r.x, midY);
	//	}
	//	return gp;
	//}

/***********************************************************************************************************************/

	private static class XGFreqBand implements Comparable<XGFreqBand>
	{	final XGValue frequency;
		final XGValue gain;
		final XGValue q;
		final XGValue shape;

		private XGFreqBand(XGValue f, XGValue g, XGValue q, XGValue s)throws XGComponentException
		{	if(f == null || g == null || q == null || s == null) throw new XGComponentException("value is null");
			this.frequency = f;
			this.gain = g;
			this.q = q;
			this.shape = s;
		}

		float getBandwidthInOctaves()
		{	float q = (float)this.q.getValue() / 10;
			return (float)(Math.log(1+1/(2*q*q)+Math.sqrt((Math.pow(2+1.0/(q*q),2))/4-1))/Math.log(2));
		}

		public int compareTo(XGFreqBand value)
		{	return this.frequency.getValue().compareTo(value.frequency.getValue());//evtl. auch freq - q(?)
		}
	}
}
