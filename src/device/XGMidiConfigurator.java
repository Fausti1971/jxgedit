package device;

import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import gui.XGFrame;
import gui.XGTree;
import xml.XMLNode;

public class XGMidiConfigurator extends XGFrame implements XGMidiConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int W = 50, H = 50, GAP = 2; 

/***************************************************************************************************************/

	private final XMLNode config;

	public XGMidiConfigurator(XMLNode cfg)
	{	super("midi");
		this.config = cfg.getChildNodeOrNew(TAG_MIDI);

		this.setLayout(new GridLayout(2, 2, 2, 2));

		XMLNode n = XMLNode.fromSet("input", XGMidi.getInputs());
		XGTree t = new XGTree(n);
		t.setSize(W, H);
/*
		inp.setBackground(root.getBackground());
		inp.setAlignmentX(0.5f);
		inp.setAlignmentY(0.5f);
	//	inp.setBorder(getDefaultBorder("input"));
		inp.setSelectedValue(this.getInputName(), true);
		inp.addListSelectionListener(new ListSelectionListener()
		{	@Override public void valueChanged(ListSelectionEvent e)
			{	if(e.getValueIsAdjusting()) return;
				JList<Info> l = (JList<Info>)e.getSource();
				setInput(l.getSelectedValue().getName());
			}
		});
*/		this.add(new JScrollPane(t));

		n = XMLNode.fromSet("output",  XGMidi.getOutputs());
		t = new XGTree(n);
		t.setSize(W, H);
/*
		JList<Info> out = new JList<>(new Vector<>(XGMidi.getOutputs()));
		out.setBackground(root.getBackground());
		out.setAlignmentX(0.5f);
		out.setAlignmentY(0.5f);
	//	out.setBorder(getDefaultBorder("output"));
		out.setSelectedValue(this.getOutputName(), true);
		out.addListSelectionListener(new ListSelectionListener()
		{	@Override public void valueChanged(ListSelectionEvent e)
			{	if(e.getValueIsAdjusting()) return;
				JList<Info> l = (JList<Info>)e.getSource();
				setOutput(l.getSelectedValue().getName());
			}
		});*/
		this.add(new JScrollPane(t));
	
		JSpinner timeout = new JSpinner();
		timeout.setAlignmentX(0.0f);
		timeout.setAlignmentY(0.5f);
	//	timeout.setBorder(getDefaultBorder("timeout"));
		timeout.setModel(new SpinnerNumberModel(this.config.parseChildNodeIntegerContent(TAG_MIDITIMEOUT, DEF_TIMEOUT), 30, 1000, 10));
		timeout.addChangeListener(new ChangeListener()
		{	@Override public void stateChanged(ChangeEvent e)
			{	JSpinner s = (JSpinner)e.getSource();
				config.getChildNodeOrNew(TAG_MIDITIMEOUT).setTextContent((int)s.getModel().getValue());
			}
		});
		timeout.setSize(W, timeout.getHeight());
		XGFrame f = new XGFrame("timeout");
		f.add(timeout);
		this.add(f);
	}

	@Override public XMLNode getConfig()
	{	return this.config;
	}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
