package gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import value.ChangeableContent;

public class XGSpinner extends JSpinner implements ChangeListener, XGUI
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**************************************************************************************************/

	private ChangeableContent<Integer> value;

	public XGSpinner(String name, ChangeableContent<Integer> v, int min, int max, int step)
	{	super(new SpinnerNumberModel((int)v.getContent(), min, max, step));
		this.value = v;
		this.addChangeListener(this);
		this.setName(name);
//		Dimension dim = new Dimension(GRID * 4, GRID * 2);
//		this.setMinimumSize(dim);
//		this.setPreferredSize(dim);
//		this.setMaximumSize(dim);

		Color c = this.getBackground().darker();
		this.setBorder(new TitledBorder(BorderFactory.createLineBorder(c, 1, true), this.getName(), TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, SMALL_FONT, c));
//		this.setAlignmentX(0.5f);
//		this.setAlignmentY(0.5f);
	}

	@Override public void stateChanged(ChangeEvent e)
	{	this.value.setContent((Integer)this.getModel().getValue());
	}
}
