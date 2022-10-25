package gui;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import application.XGMath;import static gui.XGUI.DEF_GBC;
import module.XGModule;
import tag.*;
import value.XGFixedValue;
import value.XGValue;
import value.XGValueChangeListener;


public class XGMEQ extends javax.swing.JPanel implements XGShaper, XGValueChangeListener
{
	private static final int SHELV = 0, PEAK = 1;
	private static final XGFixedValue DEF_SHAPE_VALUE = new XGFixedValue("shape", PEAK);

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
	private final int minGain, maxGain, minFreq, maxFreq;

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
			this.bands[1] = (new XGFreqBand(f2, g2, q2, DEF_SHAPE_VALUE));
			this.bands[2] = (new XGFreqBand(f3, g3, q3, DEF_SHAPE_VALUE));
			this.bands[3] = (new XGFreqBand(f4, g4, q4, DEF_SHAPE_VALUE));
			this.bands[4] = (new XGFreqBand(f5, g5, q5, s5));

			this.minFreq = f1.getParameter().getMinIndex();
			this.maxFreq = f5.getParameter().getMaxIndex();
			this.minGain = g1.getParameter().getMinIndex();
			this.maxGain = g1.getParameter().getMaxIndex();


			this.panel = new XGPointPanel(this, 1, 5, 0, 64, this.minFreq, this.maxFreq, this.minGain, this.maxGain);
			this.panel.setUnits("Frequency", "Gain");
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

		}
		catch(NullPointerException e)
		{	throw new XGComponentException(e.getMessage());
		}
	}

	public void contentChanged(XGValue v)
	{	Arrays.sort(this.bands);
		this.panel.repaint();
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();
		int midY = r.height / 2;
		int maxBandWidth = r.width / 5;
		int minBandWidth = maxBandWidth / 10;

		gp.moveTo(r.x, midY);
		if(this.bands[0].shape.getValue() == SHELV) gp.lineTo(r.x, XGMath.linearScale(this.bands[0].gain.getValue(), this.minGain, this.maxGain, r.height, r.y));

		int x, y;
		for(XGFreqBand b : this.bands)
		{	x = XGMath.linearScale(b.frequency.getValue(), this.minFreq, this.maxFreq, r.x, r.width);
			y = XGMath.linearScale(b.gain.getValue(), this.minGain, this.maxGain, r.height, r.y);
			gp.lineTo(x, y);
		}

		if(this.bands[4].shape.getValue() == SHELV) gp.lineTo(r.width, XGMath.linearScale(this.bands[4].gain.getValue(), this.minGain, this.maxGain, r.height, r.y));
		gp.lineTo(r.width, midY);

		return gp;
	}

/***********************************************************************************************************************/

	private class XGFreqBand implements Comparable<XGFreqBand>
	{	private final XGValue frequency;
		private final XGValue gain;
		private final XGValue q;
		private final XGValue shape;

		private XGFreqBand(XGValue f, XGValue g, XGValue q, XGValue s)throws XGComponentException
		{	if(f == null || g == null || q == null || s == null) throw new XGComponentException("value is null");
			this.frequency = f;
			this.gain = g;
			this.q = q;
			this.shape = s;
		}

		public int compareTo(XGFreqBand value)
		{	return this.frequency.getValue().compareTo(value.frequency.getValue());//evtl. auch freq - q(?)
		}
	}
}
