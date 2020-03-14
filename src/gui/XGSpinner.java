package gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import value.ObservableValue;

public class XGSpinner extends JSpinner implements ChangeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**************************************************************************************************/

	private ObservableValue<Integer> value;

	public XGSpinner(ObservableValue<Integer> v, int min, int max, int step)
	{	super();
		this.value = v;
		this.setModel(new SpinnerNumberModel((int)v.get(), min, max, step));
		this.addChangeListener(this);
		this.setAlignmentX(0.5f);
		this.setAlignmentY(0.5f);
	}

	@Override public void stateChanged(ChangeEvent e)
	{	this.value.set((Integer)this.getModel().getValue());
	}
}
