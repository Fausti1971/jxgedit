package gui;

import application.XGMath;import application.XGStrings;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class XGLayout implements LayoutManager2
{
	private final Dimension gridSize = new Dimension(1, 1);//aktuelle Dimension eines Grids in Pixel (ändert sich mit Größenänderung)
	private final Dimension gridCount; //Dimension des gesamten Layouts in Faktoren (ändert sich evtl. durch hinzufügen von Elementen)
	private final Map<Component, Rectangle> map = new HashMap<>();//Rectangle = Faktoren für die Berechnung der Pixels (r.x * this.gridSize.width)

	public XGLayout(Dimension grid)
	{	this.gridCount = grid;
	}

	private Dimension getGridSize(){	return new Dimension(this.gridSize.width * this.gridCount.width, this.gridSize.height * this.gridCount.height);}

/**
* Constraint o = String "x,y,w,h" oder int[]{x,y,w,h} oder Rectangle
*/
	public void addLayoutComponent(Component component, Object o)
	{	Rectangle r;
		if(o instanceof String) r = XGStrings.toRectangle((String)o);
		else if(o instanceof int[]) r = XGMath.toRectangle((int[])o);
		else if(o instanceof Rectangle) r = (Rectangle)o;
		else throw new RuntimeException("incorrect constraint (" + o.getClass().getSimpleName() + ") for component " + component.getName());
		this.map.put(component, r);
		this.gridCount.width = Math.max(this.gridCount.width, r.x + r.width);
		this.gridCount.height = Math.max(this.gridCount.height, r.y + r.height);
	}

	public Dimension maximumLayoutSize(Container container){	return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);}

	public float getLayoutAlignmentX(Container container){	return 0.5f;}

	public float getLayoutAlignmentY(Container container){	return 0.5f;}

	public void invalidateLayout(Container container){}

	public void addLayoutComponent(String s, Component component){	System.out.println("addLayoutComponent: " + component.getName());}

	public void removeLayoutComponent(Component component){	System.out.println("removeLayoutComponent: " + component.getName());}

	public Dimension preferredLayoutSize(Container container){	return this.getGridSize();}

	public Dimension minimumLayoutSize(Container container){	return this.gridSize;}

	public void layoutContainer(Container container)
	{	this.gridSize.width = Math.round((float)(container.getWidth()  / this.gridCount.width));
		this.gridSize.height = Math.round((float)(container.getHeight() / this.gridCount.height));
		Insets ins = container.getInsets();
		for(Component c : container.getComponents())
		{	Rectangle r = new Rectangle(this.map.get(c));
			r.x = r.x * this.gridSize.width + ins.left;
			r.y = r.y * this.gridSize.height + ins.top;
			r.width = r.width * this.gridSize.width - (ins.left + ins.right);
			r.height = r.height * this.gridSize.height - (ins.top + ins.bottom);
			c.setBounds(r); 
		}
	}
}
