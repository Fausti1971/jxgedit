package gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import xml.XMLNode;

public class XGDrawPanel extends JPanel implements GuiConstants
{
	private static final long serialVersionUID = 1L;

/***************************************************************************************/

	private final ArrayList<XGPoint> points = new ArrayList<>();
	private XGPoint selectedPoint;
	private final Set<Integer> vLines = new HashSet<>(), hLines = new HashSet<>();
	private int minXIndex = 0, maxXIndex = 0, minYIndex = 0, maxYIndex = 0;
	private String xUnit = "", yUnit = "";
	private Graphics2D g2;

	public XGDrawPanel(XGComponent par, XMLNode n)
	{	Rectangle r = new Rectangle(par.getBounds());
		Insets ins = par.getInsets();
		r.x = ins.left;
		r.y = ins.top;
		r.width -= (ins.left + ins.right);
		r.height -= (ins.top + ins.bottom);
		this.setLayout(null);
		this.setBounds(r);
		this.setBackground(COL_BAR_BACK);

		int l = n.getIntegerAttribute(ATTR_GRID_X, 0);
		int sect = r.height / (l + 1);
		for(int i = 1; i <= l; i++) this.hLines.add(i * sect);

		l = n.getIntegerAttribute(ATTR_GRID_Y, 0);
		sect = r.width / (l + 1);
		for(int i = 1; i <= l; i++) this.vLines.add(i * sect);
	}

	@Override public Component add(Component comp)
	{	if(comp instanceof XGPoint)
		{	XGPoint p = (XGPoint)comp;
			p.setPanel(this);
			this.points.add(p);
		}
		return super.add(comp);
	}

	void setSelectedPoint(XGPoint p)
	{	if(this.selectedPoint != null) this.selectedPoint.repaint();
		this.selectedPoint = p;
		if(this.selectedPoint != null) this.repaint();
	}

	XGPoint getSelectedPoint()
	{	return this.selectedPoint;
	}

	ArrayList<XGPoint> getPoints()
	{	return this.points;
	}

	void setUnits(String xUnit, String yUnit)
	{	this.xUnit = xUnit;
		this.yUnit = yUnit;
	}

	void setLimits(int minX, int maxX, int minY, int maxY)
	{	this.minXIndex = minX;
		this.maxXIndex = maxX;
		this.minYIndex = minY;
		this.maxYIndex = maxY;
	}

	int getMinXIndex()
	{	return this.minXIndex;
	}

	int getMinYIndex()
	{	return this.minYIndex;
	}

	int getMaxXIndex()
	{	return this.maxXIndex;
	}

	int getMaxYIndex()
	{	return this.maxYIndex;
	}

	@Override protected void paintComponent(Graphics g)
	{	for(XGPoint p : this.points) p.setLocation();
		super.paintComponent(g);
		this.g2 = (Graphics2D)g.create();
//grid
		this.g2.setColor(this.getBackground().darker());
		this.g2.setStroke(DEF_DOTTED_STROKE);
		for(int i : this.vLines) g2.drawLine(i, 0, i, this.getHeight());
		for(int i : this.hLines) g2.drawLine(0, i, this.getWidth(), i);
//polygon
		this.g2.addRenderingHints(XGComponent.AALIAS);
		this.g2.setColor(COL_SHAPE);
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
		this.g2.drawString(this.yUnit, 0, this.g2.getFontMetrics().getHeight());
		this.g2.dispose();
	}
}
