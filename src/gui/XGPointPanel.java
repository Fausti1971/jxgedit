package gui;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.*;
import javax.swing.*;
import application.XGMath;
import value.XGValue;

public class XGPointPanel extends JPanel implements XGResizeable, XGComponent, XGShaper
{
/***************************************************************************************/

	private final ArrayList<XGPoint> points = new ArrayList<>();
	private final XGShaper shaper;
	private XGValue selectedValue;
	private final Set<Integer> vLines = new HashSet<>(), hLines = new HashSet<>();
	private final int grid_x, grid_y;
	private final int minXIndex, maxXIndex, minYIndex, maxYIndex;
	private int origin_x, origin_y;
	private final int origin_x_index, origin_y_index;
	private String xUnit = "", yUnit = "";
	private Graphics2D g2;

	public XGPointPanel(XGShaper shp, int gridX, int gridY, int oriX, int oriY, int minX, int maxX, int minY, int maxY)
	{	if(shp == null) this.shaper = this;
		else this.shaper = shp;
		this.minXIndex = minX;
		this.maxXIndex = maxX;
		this.minYIndex = minY;
		this.maxYIndex = maxY;
		this.grid_x = gridX;
		this.grid_y = gridY;
		this.origin_x_index = oriX;
		this.origin_y_index = oriY;
		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
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

	XGValue getSelectedValue(){	return this.selectedValue;}

	ArrayList<XGPoint> getPoints(){	return this.points;}

	void setUnits(String xUnit, String yUnit)
	{	this.xUnit = xUnit;
		this.yUnit = yUnit;
	}

	int getMinXIndex(){	return this.minXIndex;}

	int getMinYIndex(){	return this.minYIndex;}

	int getMaxXIndex(){	return this.maxXIndex;}

	int getMaxYIndex(){	return this.maxYIndex;}

	@Override protected void paintComponent(Graphics g)
	{	super.paintComponent(g);
		this.g2 = (Graphics2D)g.create();
		for(XGPoint p : this.points) p.setLocation();
		Insets ins = this.getInsets();
		int w = this.getWidth() - (ins.left + ins.right), h = this.getHeight() - (ins.top + ins.bottom);
//background
		this.g2.setColor(COL_BAR_BACK);
		this.g2.fillRect(ins.left, ins.top, w, h);
//grid
		this.g2.setColor(Color.gray);
		this.g2.setStroke(DEF_DOTTED_STROKE);
		for(int i : this.vLines) g2.drawLine(i, ins.top, i, h);
		for(int i : this.hLines) g2.drawLine(ins.left, i, w, i);
//polygon
		this.g2.addRenderingHints(AALIAS);//keine dotted stroke mit Antialiasing
//		GradientPaint grp = new GradientPaint(0, 0, COL_BAR_BACK, 0, h, COL_SHAPE,false);
//		g2.setPaint(grp);
		GeneralPath gp = this.shaper.getShape(this.getBounds());
		this.g2.setColor(COL_SHAPE);
		this.g2.fill(gp);
//polygonline
		this.g2.setColor(COL_BAR_FORE);
		this.g2.setStroke(DEF_SMALL_STROKE);
		this.g2.draw(gp);
//units
		this.g2.setColor(this.getBackground().darker());
		this.g2.drawString(this.xUnit, w - this.g2.getFontMetrics().stringWidth(this.xUnit), h);

		this.g2.rotate(-Math.PI/2);//dreht den GraphicsContext 90° gegen UZS inkl. Koordinaten!
		this.g2.drawString(this.yUnit, -(this.g2.getFontMetrics().stringWidth(this.yUnit) + 10), this.g2.getFontMetrics().getHeight());
		this.g2.dispose();
	}

	@Override public void componentResized()
	{	this.hLines.clear();
		int sect = this.getHeight() / (this.grid_x + 1);
		for(int i = 1; i <= this.grid_x; i++) this.hLines.add(i * sect);

		this.vLines.clear();
		sect = this.getWidth() / (this.grid_y + 1);
		for(int i = 1; i <= this.grid_y; i++) this.vLines.add(i * sect);

		this.origin_x = XGMath.linearScale(this.origin_x_index, this.minXIndex, this.maxXIndex, 0, this.getWidth());
		this.origin_y = XGMath.linearScale(this.origin_y_index, this.minYIndex, this.maxYIndex, this.getHeight(), 0);

		this.repaint();
	}

	public GeneralPath getShape(Rectangle r)
	{	GeneralPath gp = new GeneralPath();
		int x = this.origin_x;
		int y = this.origin_y;
		gp.moveTo(x, y);
		for(XGPoint p : points)
		{	x = p.getX() + p.getWidth()/2;
			y = p.getY() + p.getHeight()/2;
			gp.lineTo(x, y);
		}
		gp.lineTo(r.width, this.origin_y);
//		gp.closePath();
		return gp;
	}
}