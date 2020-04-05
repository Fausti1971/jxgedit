package gui;

import java.awt.event.WindowEvent;
/**
 * qualifiziert das implementierende Object als kindfenster-besitzende, darstellbare TreeNode
 * @author thomas
 *
 */
public interface XGWindowSourceTreeNode extends XGWindowSource, XGTreeNode
{
	default boolean isValid(WindowEvent e)
	{	if(!(e.getSource() instanceof XGWindow)) return false;
		if((XGWindow)e.getSource() != this.getChildWindow()) return false;
		return true;
	}

	@Override public default void windowOpened(WindowEvent e)
	{	if(!this.isValid(e)) return;
		XGWindow w = (XGWindow)e.getSource();
		System.out.println(w + " opened");
		this.setChildWindow(w);
		this.repaintNode();
	}

	@Override public default void windowClosing(WindowEvent e)
	{	if(!this.isValid(e)) return;
		System.out.println(e.getSource() + " closing");
//		this.setStatus();
		this.reloadTree();
	}

	@Override public default void windowClosed(WindowEvent e)
	{	if(!this.isValid(e)) return;
		System.out.println(e.getSource() + " closed");
		this.setChildWindow(null);
//		((XGTreeNodeComponent)this.getGuiComponent()).setSelected(false);
		this.reloadTree();
	}

	@Override public default void windowIconified(WindowEvent e)
	{	System.out.println(e.getSource() + " iconified");
	}
	
	@Override public default void windowDeiconified(WindowEvent e)
	{	System.out.println(e.getSource() + " deiconified");
	}

	@Override public default void windowActivated(WindowEvent e)
	{	if(!this.isValid(e)) return; 
		System.out.println(e.getSource() + " activated");
//		((XGTreeNodeComponent)this.getGuiComponent()).setActive(true);
		this.reloadTree();
	}

	@Override public default void windowDeactivated(WindowEvent e)
	{	if(!this.isValid(e)) return;
		System.out.println(e.getSource() + " deactivated");
//		((XGTreeNodeComponent)this.getGuiComponent()).setSelected(this.getWindow() != null);
		this.reloadTree();
	}
}
