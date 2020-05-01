package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import adress.XGAddressableSet;
import application.Configurable;
import application.JXG;
import module.XGModule;
import value.XGValue;
import xml.XMLNode;

public interface XGComponent extends GuiConstants, Configurable, MouseListener, FocusListener
{
	XGValue DEF_VALUE = new XGValue("n/a", 0);
	RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	int AXIS_X = 1, AXIS_Y = 2, AXIS_XY = 3, AXIS_RAD = 4, DEF_AXIS = 1;

	Border defaultLineBorder = BorderFactory.createLineBorder(COL_BORDER, 1, true);
	Border focusLineBorder = BorderFactory.createLineBorder(COL_NODE_FOCUS, 1, true);

	public static XGComponent init(XGModule mod)
	{	XMLNode xml = mod.getGuiTemplate();
		XGAddressableSet<XGValue> set = mod.getDevice().getValues().getAllValid(mod.getAddress());
		if(xml == null) return new XGFrame("no template");
		if(set == null || set.isEmpty()) return new XGFrame("no values");
		return newItem(xml, set);
	}

	private static XGComponent newItem(XMLNode n, XGAddressableSet<XGValue> set)
	{	String s = n.getTag();
		XGComponent c = new XGFrame("unknown " + s);
		switch(s)
		{	case TAG_AUTO:		break;
			case TAG_ENVELOPE:	c = new XGEnvelope(n, set); break;
			case TAG_ENVPOINT:	break;
			case TAG_FRAME:		c = new XGFrame(n, set); break;
			case TAG_KNOB:		c = new XGKnob(n, set); break;
			case TAG_SLIDER:	c = new XGSlider(n, set); break;
			default:			break;
		}
		for(XMLNode x : n.getChildNodes()) c.addGB(newItem(x, set));
		return c;
	}

	static int getAxis(String s)
	{	if(s != null)
		{	if(s.equals("x")) return AXIS_X;
			if(s.equals("y")) return AXIS_Y;
			if(s.equals("xy")) return AXIS_XY;
			if(s.equals("rad")) return AXIS_RAD;
		}
		return DEF_AXIS;
	}

/********************************************************************************************/

	public JComponent getJComponent();

/**
 * erfragt die XML-Attribute ATTR_GB_W (grid_w) und ATTR_GB_H (grid_h) und setzt die minimum- und prefferedSize der Komponente; bei nicht vorhandenen Werten werden die übergebenen Default-Werte verwendet;
 * @param pref_w Default-Komponent-Breite
 * @param pref_h Default-Komponent-Höhe
 */
	public default void setSizes(int pref_w, int pref_h)
	{	XMLNode n = this.getConfig();
		Dimension dim = new Dimension(n.getIntegerAttribute(ATTR_GB_W, pref_w), n.getIntegerAttribute(ATTR_GB_H,  pref_h));
		this.getJComponent().setMinimumSize(dim);
		this.getJComponent().setPreferredSize(dim);
	}

	public default void borderize()
	{	JComponent c = this.getJComponent();
		String name = c.getName();
		if(c.hasFocus())
			c.setBorder(new TitledBorder(focusLineBorder, name, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_NODE_FOCUS));
		else
			c.setBorder(new TitledBorder(defaultLineBorder, name, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, FONT, COL_BORDER));
	}

	public default void deborderize()
	{	this.getJComponent().setBorder(null);
	}

	@Override public default void mouseClicked(MouseEvent e)
	{	if(e.getClickCount() == 2)
		{	System.out.println("doubleclick detected");
		}
	}

	@Override public default void mousePressed(MouseEvent e)
	{	JXG.dragEvent = e;
		e.consume();
	}

	@Override public default void mouseReleased(MouseEvent e)
	{	JXG.dragEvent = e;
	}

	@Override public default void mouseEntered(MouseEvent e)
	{	if(JXG.dragEvent == null || JXG.dragEvent.getID() == MouseEvent.MOUSE_RELEASED) this.getJComponent().requestFocusInWindow();
	}

	@Override public default void mouseExited(MouseEvent e)
	{
	}

	@Override public default void focusLost(FocusEvent e)
	{	this.borderize();
		this.getJComponent().repaint();
	}

	@Override public default void focusGained(FocusEvent e)
	{	this.borderize();
		this.getJComponent().repaint();
	}

	public default void addGB(XGComponent c)
	{	XMLNode n = c.getConfig();
		this.addGB
		(	c.getJComponent(),
			n.getIntegerAttribute(ATTR_GB_X, GridBagConstraints.RELATIVE),
			n.getIntegerAttribute(ATTR_GB_Y, GridBagConstraints.RELATIVE),
			n.getIntegerAttribute(ATTR_GB_W, 1),
			n.getIntegerAttribute(ATTR_GB_H, 1),
			n.getIntegerAttribute(ATTR_GB_FILL, GridBagConstraints.BOTH),
			n.getDoubleAttribute(ATTR_GB_WEIGHT_X, 0.5),
			n.getDoubleAttribute(ATTR_GB_WEIGHT_Y, 0.5),
			n.getIntegerAttribute(ATTR_GB_ANCHOR, GridBagConstraints.WEST),
			new Insets(0, 0, 0, 0),
			n.getIntegerAttribute(ATTR_GB_PAD_X, 0),
			n.getIntegerAttribute(ATTR_GB_PAD_Y, 0)
		);
	}

/**Bequemlichkeitsmethoden für das Hinzufügen von Komponenten mit den benötigten GridBagConstraints:
 * 
 * @param component
 * @param gridx
 * @param gridy
 */
	public default void addGB(Component component, int gridx, int gridy)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public default void addGB(Component component, int gridx, int gridy, int gridwidth)
	{	addGB(component, gridx, gridy, gridwidth, 1, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public default void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight)
	{	addGB(component, gridx, gridy, gridwidth, gridheight, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public default void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill)
	{	addGB(component, gridx, gridy, gridwidth, gridheight, fill, 0.5, 0.5, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public default void addGB(Component component, int gridx, int gridy, double weightx, double weighty)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public default void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int ipadx, int ipady)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, GridBagConstraints.NORTHWEST, new Insets(0, 0, 0, 0), ipadx, ipady);
	}

	public default void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int anchor)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, anchor, new Insets(0, 0, 0, 0), 0, 0);
	}

	public default void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int anchor, Insets insets)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, anchor, insets, 0, 0);
	}
/**
 * 
 * @param component
 * @param gridx
 * @param gridy	Das sind int-Werte, welche die Koordinaten der oberen linken Zelle einer Komponente im Raster darstellen, wobei gridx die Spalte festlegt und gridy die Zeile. Der Defaultwert RELATIVE bedeutet, dass diese Komponente neben/unterhalb der zuvor hinzugefügten Komponente platziert wird (dies wird gewöhnlich nicht empfohlen)
 * @param gridwidth
 * @param gridheight	Gibt die Anzahl der Zellen in einer Zeile / Spalte für die Komponente an. Der Standardwert ist 1. Wir verwenden REMAINDER um anzugeben, dass die Komponente von gridx / gridy bis zur letzten Zelle in der Zeile / Spalte reicht. Wenn wir mit relativen Koordinaten arbeiten, können wir die letzte Komponente jeder Zeile mit gridwidth = REMAINDER markieren
 * @param fill	Wenn der Anzeigebereich einer Komponente größer ist als die gewünschte Größe der Komponente (preferredSize / minimumSize), können wir die Komponente horizontal, vertikal oder in beide Richtungen skalieren. Die folgenden Werte sind gültig für die "fill" Eigenschaft:    * NONE: die Komponente nicht skalieren
	HORIZONTAL: die Komponente breit genug machen, um ihren Anzeigebereich horizontal zu füllen
	VERTICAL: die Komponente groß genug machen, um ihren Anzeigebereich vertikal zu füllen
	BOTH: die Komponente soll den ganzen Anzeigebereich füllen
 * @param weightx
 * @param weighty	Damit die Komponenten nicht im Zentrum des Panels gebündelt bleiben, sondern sich über die zur Verfügung stehende Fläche verteilen können, müssen wir dem Layout sagen, wie der zusätzliche Platz verteilt werden soll. Das geschieht mit weightx zum Verteilen des horizontalen Raums und weighty zum Verteilen des vertikalen Raums. Der zusätzliche Raum wird auf jede Spalte / Zeile im Verhältnis zum Gewicht verteilt.
	Der Standardwert für beide Felder ist 0, was bedeutet, dass die Spalte / Zeile nie zusätzlichen Raum erhält. Es genügt, wenn wir das Gewicht für eine einzige Komponente einer Spalte / Zeile angeben. Der Layout-Manager setzt das Gewicht der Spalte / Zeile gleich dem maximalen Gewicht aller Komponenten in dieser Spalte / Zeile.
	Falls mehrzellige Komponenten benutzt werden, geht der extra Raum tendenziell in Richtung der Spalte ganz rechts und untere Zeile.
	In der Regel werden die Gewichte mit 0.0 und 1.0 als die Extreme angegeben: die Zahlen dazwischen verwenden wir so wie es nötig ist. Höhere Werte zeigen an, dass die Zeile oder Spalte mehr Platz bekommen sollte. Was zählt, ist jedoch nicht der absolute Wert, sondern das Verhältnis der Werte zueinander.
 * @param anchor	Dieses Feld wird verwendet, wenn die Komponente kleiner ist als ihr Anzeigebereich. Damit bestimmen wir, wo die Komponente innerhalb ihrer Anzeigefläche platziert wird. Standardmäßig wird sie zentriert.
	Es gibt drei Arten von möglichen Werten: orientierungsbezogene, grundlinienbezogene und absolute Werte. Orientierungsbezogene Werte beziehen sich auf die Komponentenorientierung des Containers (seit JDK 1.4), grundlinienbezogenen Werte beziehen sich auf die Grundlinie (Baseline, seit JDK 1.6).
	Die absoluten Werte sind: CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, und NORTHWEST.
	Die orientierungsbezogenen Werte sind: PAGE_START, PAGE_END, LINE_START, LINE_END, FIRST_LINE_START, FIRST_LINE_END, LAST_LINE_START und LAST_LINE_END.
	Die grundlinienbezogene Werte sind: BASELINE, BASELINE_LEADING, BASELINE_TRAILING, ABOVE_BASELINE, ABOVE_BASELINE_LEADING, ABOVE_BASELINE_TRAILING, BELOW_BASELINE, BELOW_BASELINE_LEADING und BELOW_BASELINE_TRAILING.
	Der Standardwert ist CENTER. Die Verwendung der orientierungsbezogenen Werte, sowie CENTER, wird empfohlen:
	FIRST_LINE_START, PAGE_START, FIRST_LINE_END, LINE_START, CENTER, LINE_END, LAST_LINE_START, PAGE_END, LAST_LINE_END
 * @param insets	Dieses Feld gibt die Außenpolsterung der Komponente an, das heißt den Mindestabstand zwischen der Komponente und den vier Kanten ihres Anzeigebereichs. Der Standardwert ist: new Insets (0, 0, 0, 0), was bedeutet, dass die Komponente keine Außenpolsterung hat.
 * @param ipadx
 * @param ipady	Diese Felder geben die Innenpolsterung der Komponente an, das heißt den Raum, welcher der Komponente hinzugefügt wird, wodurch diese größer wird. Dabei wird die Breite der Komponente mit ipadx gepolstert und ihre Höhe mit ipady.
	Der Standardwert für beide Felder ist 0, was bedeutet, dass die Komponente keine Innenpolsterung hat.
 */
	public default void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill, double weightx, double weighty, int anchor, Insets insets, int ipadx, int ipady)
	{	GBCONSTRAINTS.gridx = gridx;
		GBCONSTRAINTS.gridy = gridy;
		GBCONSTRAINTS.gridwidth = gridwidth;
		GBCONSTRAINTS.gridheight = gridheight;
		GBCONSTRAINTS.fill = fill;
		GBCONSTRAINTS.weightx = weightx;
		GBCONSTRAINTS.weighty = weighty;
		GBCONSTRAINTS.anchor = anchor;
		GBCONSTRAINTS.insets = insets;
		GBCONSTRAINTS.ipadx = ipadx;
		GBCONSTRAINTS.ipady = ipady;
		this.getJComponent().add(component, GBCONSTRAINTS);
	}

}
