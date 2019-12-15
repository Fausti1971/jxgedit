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
		if((XGWindow)e.getSource() != this.getWindow()) return false;
		return true;
	}

	public default void windowOpened(WindowEvent e)
	{	if(!this.isValid(e)) return;
		XGWindow w = (XGWindow)e.getSource();
		System.out.println(w + " opened");
		this.setWindow(w);
		this.repaintNode();
	}

	public default void windowClosing(WindowEvent e)
	{	if(!this.isValid(e)) return;
		System.out.println(e.getSource() + " closing");
//		this.setStatus();
		this.reloadTree();
	}

	public default void windowClosed(WindowEvent e)
	{	if(!this.isValid(e)) return;
		System.out.println(e.getSource() + " closed");
		this.setWindow(null);
//		((XGTreeNodeComponent)this.getGuiComponent()).setSelected(false);
		this.reloadTree();
	}

	public default void windowIconified(WindowEvent e)
	{	System.out.println(e.getSource() + " iconified");
	}
	
	public default void windowDeiconified(WindowEvent e)
	{	System.out.println(e.getSource() + " deiconified");
	}

	public default void windowActivated(WindowEvent e)
	{	if(!this.isValid(e)) return; 
		System.out.println(e.getSource() + " activated");
//		((XGTreeNodeComponent)this.getGuiComponent()).setActive(true);
		this.reloadTree();
	}

	public default void windowDeactivated(WindowEvent e)
	{	if(!this.isValid(e)) return;
		System.out.println(e.getSource() + " deactivated");
//		((XGTreeNodeComponent)this.getGuiComponent()).setSelected(this.getWindow() != null);
		this.reloadTree();
	}
}
