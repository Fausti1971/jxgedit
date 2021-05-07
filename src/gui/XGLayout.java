package gui;

import static application.XGLoggable.LOG;import application.XGStrings;
import java.awt.*;import java.util.HashMap;import java.util.Map;

public class XGLayout implements LayoutManager2
{
	private final Dimension grid,//aktuelle Dimension eines Grids in Pixel
		gridCount = new Dimension(1,1); //Dimension des gesamten Layouts in Faktoren
		
	private final Map<Component, Rectangle> map = new HashMap<>();//Rectangle = Faktoren für die Berechnung der Pixels (r.x * this.grid.width)

	public XGLayout(Dimension dim)
	{	this.grid = dim;
	}
/**
* Constraint o = String "LORU" X=alpha, Y= num ("A2B4" = X.links=0, Y.oben=2, X.rechts=1, Y.unten=4)
*/
	public void addLayoutComponent(Component component, Object o)
	{	if(!(o instanceof String))
		{	LOG.severe("incorrect constraint (" + o + ") for component " + component.getName());
			return;
		};
		Rectangle r = XGStrings.toGrid((String)o);
		this.map.put(component, r);
		this.gridCount.width = Math.max(this.gridCount.width, r.x + r.width);
		this.gridCount.height = Math.max(this.gridCount.height, r.y + r.height);
	}

	public Dimension maximumLayoutSize(Container container)
	{	return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public float getLayoutAlignmentX(Container container)
	{	return 0;
	}

	public float getLayoutAlignmentY(Container container)
	{	return 0;
	}

	public void invalidateLayout(Container container)
	{	this.layoutContainer(container);
	}

	public void addLayoutComponent(String s,Component component)
	{	System.out.println("addLayoutComponent: " + component.getName());
	}

	public void removeLayoutComponent(Component component)
	{	System.out.println("removeLayoutComponent: " + component.getName());
	}

	public Dimension preferredLayoutSize(Container container)
	{	Insets ins = container.getInsets();
		return new Dimension(this.gridCount.width * this.grid.width + ins.left + ins.right, this.gridCount.height * this.grid.height + ins.top + ins.bottom);
	}

	public Dimension minimumLayoutSize(Container container)
	{	return this.preferredLayoutSize(container);
	}

	public void layoutContainer(Container container)
	{	Insets ins = container.getInsets();
		//Dimension newSize = container.getSize();
		//this.grid.width = newSize.width / this.gridCount.width;
		//this.grid.height = newSize.height / this.gridCount.height;
		for(Component c : container.getComponents())
		{	Rectangle r = new Rectangle(this.map.get(c));
			r.x *= this.grid.width;
			r.x += ins.left;
			r.y *= this.grid.height;
			r.y += ins.top;
			r.width *= this.grid.width;
			r.height *= this.grid.height;
			c.setBounds(r); 
		}
		container.setPreferredSize(this.preferredLayoutSize(container));
//		LOG.info("layoutContainer (" + container.getName() + ")");
	}
}
