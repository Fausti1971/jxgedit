package gui;

import module.XGInsertionModule;import value.XGValue;import value.XGValueChangeListener;import xml.XMLNode;import java.beans.PropertyChangeEvent;import java.beans.PropertyChangeListener;

public class XGInsertionTabbedFrame extends XGTabbedFrame implements XGValueChangeListener, PropertyChangeListener
{
	final XGInsertionModule module;
	final XGValue program;

	public XGInsertionTabbedFrame(XMLNode node, XGInsertionModule mod)
	{	super(node);
		this.module = mod;
		this.program = mod.getProgram();
		this.program.getValueListeners().add(this);
		this.addPropertyChangeListener(this);
	}

	public void contentChanged(XGValue v)
	{	if(this.getTabCount() != 2) return;
		if(this.module.isMSBRequired())
		{	this.setEnabledAt(0, false);
			this.setEnabledAt(1, true);
			this.setSelectedIndex(1);
		}
		else
		{	this.setEnabledAt(0, true);
			this.setEnabledAt(1, false);
			this.setSelectedIndex(0);
		}
	}

	public void propertyChange(PropertyChangeEvent event){	this.contentChanged(this.program);}//damit die Komponente mit ihrem ersten Sichtbarwerden initialisiert wird 
}