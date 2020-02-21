package gui;
//TODO nachdenken:	XGAddress hi > 127 für interne Prozesse mit eigenen Parametern und Values missbrauchen (z.B. hi 128 = JXG, hi 129 = XGDevice; lo-addresses durchnummerieren)
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import device.XGDeviceConstants;
import device.XGMidiConfigurator;
import file.XGSysexFile;
import xml.XMLNode;

public class XGDeviceConfigurator extends XGFrame implements XGComponent, XGDeviceConstants
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/***************************************************************************************************************/

	private final XMLNode config;

	public XGDeviceConfigurator(XMLNode cfg)
	{	super("device");
		if(cfg == null) cfg = new XMLNode(TAG_DEVICE, null);
		this.config = cfg;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new XGMidiConfigurator(this.config));

		JSpinner sp = new JSpinner();
//			sp.setBorder(getDefaultBorder("sysex ID"));
		sp.setModel(new SpinnerNumberModel(this.config.parseChildNodeIntegerContent(TAG_SYSEXID, 0), 0, 15, 1));
		sp.addChangeListener(new ChangeListener()
		{	@Override public void stateChanged(ChangeEvent e)
			{	JSpinner s = (JSpinner)e.getSource();
				config.getChildNodeOrNew(TAG_SYSEXID).setTextContent((int)s.getModel().getValue());
			}
		});
		this.add(sp);

		String s = this.config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).getTextContent();
		JButton btn = new JButton(s);
		btn.setAlignmentX(0.5f);
		btn.addActionListener(new ActionListener()
		{	@Override public void actionPerformed(ActionEvent e)
			{	XGSysexFile f = new XGSysexFile(null, s);
				Path p = f.selectPath(f.toString());
				config.getChildNodeOrNew(TAG_DEFAULTDUMPFOLDER).setTextContent(p.toString());
				btn.setText(p.toString());
				btn.getTopLevelAncestor().revalidate();
			}
		});
		this.add(btn);

//		this.setVisible(true);
	}

	@Override public void setForeground(Color col)
	{
	}

	@Override public void setBackground(Color col)
	{
	}

	@Override public JComponent getJComponent()
	{	return this;
	}
}
