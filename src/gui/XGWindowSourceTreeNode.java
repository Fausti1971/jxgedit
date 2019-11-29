package gui;

import java.awt.event.WindowEvent;
import gui.XGTreeNodeComponent.Status;
/**
 * qualifiziert das implementierende Object als kindfenster-besitzende, darstellbare TreeNode
 * @author thomas
 *
 */
public interface XGWindowSourceTreeNode extends XGWindowSource, XGTreeNode
{
	public default void windowOpened(WindowEvent e)
	{	System.out.println(e.getSource() + " opened");
		this.setWindow((XGWindow)e.getSource());
		((XGTreeNodeComponent)this.getGuiComponent()).setStatus(Status.active);
		this.reloadTree(this.getTree());
	}

	public default void windowClosing(WindowEvent e)
	{	System.out.println(e.getSource() + " closing");
		((XGWindow)e.getSource()).dispose();
		((XGTreeNodeComponent)this.getGuiComponent()).setStatus(Status.unselected);
		this.reloadTree(this.getTree());
	}

	public default void windowClosed(WindowEvent e)
	{	System.out.println(e.getSource() + " closed");
		this.setWindow(null);
		((XGTreeNodeComponent)this.getGuiComponent()).setStatus(Status.unselected);
		this.reloadTree(this.getTree());
	}

	public default void windowIconified(WindowEvent e)
	{	System.out.println(e.getSource() + " iconified");
	}
	
	public default void windowDeiconified(WindowEvent e)
	{	System.out.println(e.getSource() + " deiconified");
	}

	public default void windowActivated(WindowEvent e)
	{	System.out.println(e.getSource() + " activated");
		((XGTreeNodeComponent)this.getGuiComponent()).setStatus(Status.active);
		this.reloadTree(this.getTree());
	}

	public default void windowDeactivated(WindowEvent e)
	{	System.out.println(e.getSource() + " deactivated");
		Status s = Status.selected;
		if(this.getWindow() == null) s = Status.unselected;
		((XGTreeNodeComponent)this.getGuiComponent()).setStatus(s);
		this.reloadTree(this.getTree());
	}
}
