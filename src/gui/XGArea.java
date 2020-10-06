package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import module.XGModule;
import parm.XGTable;
import value.XGFixedValue;
import xml.XMLNode;

public class XGArea extends JPanel implements GuiConstants
{
	private static final long serialVersionUID = 1L;

/*****************************************************************************************/

	private ArrayList<XGPoint> points = new ArrayList<>();
	private final Set<Integer> vLines = new HashSet<>(), hLines = new HashSet<>();
	private ButtonGroup group = new ButtonGroup();
	private Integer minXIndex = null, maxXIndex = null, minYIndex = null, maxYIndex = null;
	private XGTable xTable, yTable;
	private String xUnit = "", yUnit = "";
	private Graphics2D g2;

	public XGArea(XGComponent par, XGModule mod)
	{	super();
		this.setLayout(null);
		this.setBackground(COL_BAR_BACK);

		Rectangle r = new Rectangle(par.getBounds());
		Insets ins = par.getInsets();
		r.x = ins.left;
		r.y = ins.top;
		r.width -= (ins.left + ins.right);
		r.height -= (ins.top + ins.bottom);
		this.setBounds(r);

		int hl = par.getConfig().getIntegerAttribute(ATTR_LINES_H, 0);
		int vl = par.getConfig().getIntegerAttribute(ATTR_LINES_V, 0);
		int sect = r.height / (hl + 1);
		for(int i = 1; i <= hl; i++) this.hLines.add(i * sect);
		sect = r.width / (vl + 1);
		for(int i = 1; i <= vl; i++) this.vLines.add(i * sect);

		for(XMLNode pn : par.getConfig().getChildNodes(TAG_POINT))
		{	this.addPoint(new XGPoint(this, pn, mod));
		}

		for(XGPoint p : this.points) p.setLocation();
	}

	void addPoint(XGPoint p)
	{	if(!(p.getValueX() instanceof XGFixedValue))
		{	this.setXTable(p.getValueX().getParameter().getTranslationTable());
			this.setXLimits(p);
		}
		if(!(p.getValueY() instanceof XGFixedValue))
		{	this.setYTable(p.getValueY().getParameter().getTranslationTable());
			this.setYLimits(p);
		}
		this.points.add(p);
		this.group.add(p);
		this.add(p);
	}

	private void setXTable(XGTable tab)
	{	if(this.xTable == null) this.xTable = tab;
		else if(!this.xTable.equals(tab)) throw new RuntimeException("x-Tables are not equal: old=" + this.xTable + " new=" + tab);
		if(tab.getUnit() != null && !tab.getUnit().isBlank()) this.xUnit = tab.getUnit();
	}

	private void setYTable(XGTable tab)
	{	if(this.yTable == null) this.yTable = tab;
		else if(!this.yTable.equals(tab)) throw new RuntimeException("y-Tables are not equal: old=" + this.yTable + " new=" + tab);
		if(tab.getUnit() != null && !tab.getUnit().isBlank()) this.yUnit = tab.getUnit();
	}

	private void setXLimits(XGPoint p)
	{	int min = p.getValueX().getParameter().getMinIndex();
		int max = p.getValueX().getParameter().getMaxIndex();

		if(this.minXIndex == null) this.minXIndex = min;
		else this.minXIndex = Math.min(this.minXIndex, min);

		if(this.maxXIndex == null) this.maxXIndex = max;
		else this.maxXIndex = Math.max(this.maxXIndex, max);

		this.xUnit = p.getValueX().getParameter().getUnit();
	}

	private void setYLimits(XGPoint p)
	{	int min = p.getValueY().getParameter().getMinIndex();
		int max = p.getValueY().getParameter().getMaxIndex();

		if(this.minYIndex == null) this.minYIndex = min;
		else this.minYIndex = Math.min(this.minYIndex, min);

		if(this.maxYIndex == null) this.maxYIndex = max;
		else this.maxYIndex = Math.max(this.maxYIndex, max);

		this.yUnit = p.getValueY().getParameter().getUnit();
	}

	int getMinXIndex()
	{	if(this.minXIndex != null) return this.minXIndex;
		else return 0;
	}

	int getMinYIndex()
	{	if(this.minYIndex != null) return this.minYIndex;
		else return 0;
	}

	int getMaxXIndex()
	{	if(this.maxXIndex != null) return this.maxXIndex;
		else return 0;
	}

	int getMaxYIndex()
	{	if(this.maxYIndex != null) return this.maxYIndex;
		else return 0;
	}

	@Override protected void paintComponent(Graphics g)
	{	super.paintComponent(g);
		this.g2 = (Graphics2D)g.create();
//grid
		this.g2.setColor(this.getBackground().darker());
		this.g2.setStroke(DEF_DOTTED_STROKE);
		for(int i : this.vLines) g2.drawLine(i, 0, i, this.getHeight());
		for(int i : this.hLines) g2.drawLine(0, i, this.getWidth(), i);
//polygon
		this.g2.addRenderingHints(XGComponent.AALIAS);
		this.g2.setColor(new Color(COL_BAR_FORE.getRed(), COL_BAR_FORE.getGreen(), COL_BAR_FORE.getBlue(), 40));
		GeneralPath gp = new GeneralPath();
		int x = 0;
		int y = this.getHeight();
		gp.moveTo(x, y);
		for(XGPoint p : this.points)
		{	x = p.getX() + p.getWidth()/2;
			y = p.getY() + p.getHeight()/2;
			gp.lineTo(x, y);
		}
		gp.lineTo(this.getWidth(), y);
		gp.lineTo(this.getWidth(), this.getHeight());
		gp.closePath();
		this.g2.fill(gp);
//		this.g2.draw(gp);
//units
		this.g2.setFont(FONT);
		this.g2.setColor(this.getBackground().darker());
		this.g2.drawString(this.xUnit, this.getWidth() - this.g2.getFontMetrics().stringWidth(this.xUnit), this.getHeight());
		this.g2.drawString(this.yUnit, 0, this.g2.getFontMetrics().stringWidth(this.yUnit));
		this.g2.dispose();
	}
}
