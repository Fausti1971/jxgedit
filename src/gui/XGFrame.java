package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import value.XGValue;
import xml.XMLNode;

public class XGFrame extends JPanel implements XGBorderable
{	/**
	 * 
	 */
	private static final long serialVersionUID=-2090844398565572567L;

/********************************************************************************************************************/

//	private int gridx, gridy, gridwidth, gridheight, fill, anchor, ipadx, ipady;
//	private double weightx, weighty;
//	private Insets insets;

	public XGFrame(String text)
	{	super(new GridBagLayout());
		this.setName(text);
//		this.colorize();
		this.borderize();
	}

	public XGFrame(XMLNode n, Set<XGValue> set)
	{	this(n.getStringAttribute(ATTR_NAME) + " " + set.size());
	}

	@Override public Component add(Component c)
	{	this.addGB(c, GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE);
		return c;
	}

/**Bequemlichkeitsmethoden für das Hinzufügen von Komponenten mit den benötigten GridBagConstraints:
 * 
 * @param component
 * @param gridx
 * @param gridy
 */
	public void addGB(Component component, int gridx, int gridy)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public void addGB(Component component, int gridx, int gridy, int gridwidth)
	{	addGB(component, gridx, gridy, gridwidth, 1, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight)
	{	addGB(component, gridx, gridy, gridwidth, gridheight, GridBagConstraints.BOTH, 0.5, 0.5, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill)
	{	addGB(component, gridx, gridy, gridwidth, gridheight, fill, 0.5, 0.5, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public void addGB(Component component, int gridx, int gridy, double weightx, double weighty)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), 0, 0);
	}

	public void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int ipadx, int ipady)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), ipadx, ipady);
	}

	public void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int anchor)
	{	addGB(component, gridx, gridy, 1, 1, GridBagConstraints.BOTH, weightx, weighty, anchor, new Insets(0, 0, 0, 0), 0, 0);
	}

	public void addGB(Component component, int gridx, int gridy, double weightx, double weighty, int anchor, Insets insets)
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
 * @param ipady	Diese Felder geben die Innenpolsterung der Komponente an, das heißt den Raum, welcher der Komponente hinzugefügt wird, wodurch diese größer wird. Dabei wird die Breite der Komponente mit ipadx gepolstert und ihre Höhe mit ipadyFetter Text.
	Der Standardwert für beide Felder ist 0, was bedeutet, dass die Komponente keine Innenpolsterung hat.
 */
	public void addGB(Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill, double weightx, double weighty, int anchor, Insets insets, int ipadx, int ipady)
	{	GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.fill = fill;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.anchor = anchor;
		constraints.insets = insets;
		constraints.ipadx = ipadx;
		constraints.ipady = ipady;
		add(component, constraints);
	}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
