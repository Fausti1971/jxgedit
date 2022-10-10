package gui;

import static application.XGLoggable.LOG;import application.XGMath;import application.XGStrings;
import javax.swing.*;import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class XGLayout implements LayoutManager2
{
	static Rectangle constraintsObjectToRectangle(Object o)
	{	if(o == null) throw new RuntimeException("constraint object is null!");
		if(o instanceof String) return XGStrings.toRectangle((String)o);
		else if(o instanceof int[]) return XGMath.toRectangle((int[])o);
		else if(o instanceof Rectangle) return new Rectangle((Rectangle)o);
		else throw new RuntimeException("incorrect constraint object (" + o.getClass().getSimpleName() + ")");
	}

/***********************************************************************************************************************/

	private int rows, columns; //Dimension des gesamten Layouts in Faktoren von gridSize(ändert sich evtl. durch hinzufügen von Elementen)
	private final Map<Component, Rectangle> map = new HashMap<>();//Rectangle = Faktoren für die Berechnung der Pixels (r.x * this.gridSize.width)

	public XGLayout(Dimension grid)
	{	this.rows = grid.height;
		this.columns = grid.width;
	}

/**
* Constraint o = String "x,y,w,h" oder int[]{x,y,w,h} oder Rectangle
*/
	public void addLayoutComponent(Component component, Object o)
	{	Rectangle r = constraintsObjectToRectangle(o);
		for(Rectangle rect : this.map.values())
			if(rect.intersects(r))
				JOptionPane.showMessageDialog(XGMainWindow.MAINWINDOW, r + " hides the component at " + rect);
		this.map.put(component, r);
		this.columns = Math.max(this.columns, r.x + r.width);
		this.rows = Math.max(this.rows, r.y + r.height);
	}

	public Dimension maximumLayoutSize(Container container){	return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);}

	public float getLayoutAlignmentX(Container container){	return 0.5f;}

	public float getLayoutAlignmentY(Container container){	return 0.5f;}

	public void invalidateLayout(Container container){}

	public void addLayoutComponent(String s, Component component){	System.out.println("addLayoutComponent: " + component.getName());}

	public void removeLayoutComponent(Component component){	this.map.remove(component);}

	public Dimension preferredLayoutSize(Container container){	return new Dimension(this.columns, this.rows);}

	public Dimension minimumLayoutSize(Container container){	return new Dimension(this.columns, this.rows);}

	public void layoutContainer(Container container)
	{	Insets ins = container.getInsets();
		float colWidth = Math.round((float)((container.getWidth() - (ins.left + ins.right))  / this.columns));
		float rowHeight = Math.round((float)((container.getHeight() - (ins.top + ins.bottom)) / this.rows));
		Rectangle r;
		for(Component c : container.getComponents())
		{	r = this.map.get(c);
			c.setBounds(Math.round(r.x * colWidth + ins.left), Math.round(r.y * rowHeight + ins.top), Math.round(r.width * colWidth), Math.round(r.height * rowHeight));
		}
	}
}
