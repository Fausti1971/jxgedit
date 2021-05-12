package gui;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import application.XGMath;
import value.XGValue;
import xml.XMLNode;

public class XGPointPanel extends JPanel implements XGUI, XGResizeable, XGComponent
{
	private static final long serialVersionUID = 1L;
	private static Dimension MIN_DIM = new java.awt.Dimension(5 * 66, 2 * 88);

/***************************************************************************************/

	private final ArrayList<XGPoint> points = new ArrayList<>();
	private XGValue selectedValue;
	private final Set<Integer> vLines = new HashSet<>(), hLines = new HashSet<>();
	private final int grid_x, grid_y;
	private final int minXIndex, maxXIndex, minYIndex, maxYIndex;
	private int origin_x, origin_y;
	private final int origin_x_index, origin_y_index;
	private String xUnit = "", yUnit = "";
	private Graphics2D g2;

	public XGPointPanel(int gridX, int gridY, int oriX, int oriY, int minX, int maxX, int minY, int maxY)
	{
		this.setLayout(null);
		this.minXIndex = minX;
		this.maxXIndex = maxX;
		this.minYIndex = minY;
		this.maxYIndex = maxY;
		this.grid_x = gridX;
		this.grid_y = gridY;
		this.origin_x_index = oriX;
		this.origin_y_index = oriY;
		this.setPreferredSize(MIN_DIM);
		this.setMinimumSize(MIN_DIM);
		this.addComponentListener(this);
	}

	@Override public Component add(Component comp)
	{	if(comp instanceof XGPoint)
		{	XGPoint p = (XGPoint)comp;
			p.setPanel(this);
			this.points.add(p);
		}
		return super.add(comp);
	}

	void setSelectedValue(XGValue v)
	{	this.selectedValue = v;
		this.repaint();
	}

	XGValue getSelectedValue()
	{	return this.selectedValue;
	}

	ArrayList<XGPoint> getPoints()
	{	return this.points;
	}

	void setUnits(String xUnit, String yUnit)
	{	this.xUnit = xUnit;
		this.yUnit = yUnit;
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
//background
		int w = this.getWidth(), h = this.getHeight();
		this.g2.setColor(COL_BAR_BACK);
		this.g2.fillRect(0, 0, w, h);

//grid
		this.g2.setColor(java.awt.Color.gray);
		this.g2.setStroke(DEF_DOTTED_STROKE);
		for(int i : this.vLines) g2.drawLine(i, 0, i, h);
		for(int i : this.hLines) g2.drawLine(0, i, w, i);
//polygon
		this.g2.addRenderingHints(AALIAS);
		this.g2.setColor(COL_SHAPE);
		GeneralPath gp = new GeneralPath();
		int x = this.origin_x;
		int y = this.origin_y;
		gp.moveTo(x, y);
		for(XGPoint p : this.points)
		{	x = p.getX() + p.getWidth()/2;
			y = p.getY() + p.getHeight()/2;
			gp.lineTo(x, y);
		}
		gp.lineTo(w, this.origin_y);
//		gp.lineTo(this.origin_x, this.getHeight());
		gp.closePath();
		this.g2.fill(gp);
//		this.g2.draw(gp);
//units
		this.g2.setFont(SMALL_FONT);
		this.g2.setColor(this.getBackground().darker());
		this.g2.drawString(this.xUnit, w - this.g2.getFontMetrics().stringWidth(this.xUnit), h);
		this.g2.drawString(this.yUnit, 0, this.g2.getFontMetrics().getHeight());
		this.g2.dispose();
	}

	@Override public void componentResized()
	{	this.hLines.clear();
		int sect = this.getHeight() / (this.grid_x + 1);
		for(int i = 1; i <= this.grid_x; i++) this.hLines.add(i * sect);

		this.vLines.clear();
		sect = this.getWidth() / (this.grid_y + 1);
		for(int i = 1; i <= this.grid_y; i++) this.vLines.add(i * sect);

		this.origin_x = XGMath.linearIO(this.origin_x_index, this.minXIndex, this.maxXIndex, 0, this.getWidth());
		this.origin_y = XGMath.linearIO(this.origin_y_index, this.minYIndex, this.maxYIndex, this.getHeight(), 0);

		this.repaint();
	}
}
