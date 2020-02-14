package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public interface XGAction extends ActionListener
{	public Set<String> getActions();

	public default void performDefaultAction()
	{	if(this.getActions().isEmpty()) return;
		this.actionPerformed(new ActionEvent(this, 0, this.getActions().iterator().next()));
	}
}
