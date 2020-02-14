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
import application.ConfigurationConstants;
import device.XGDevice;
import file.XGSysexFile;
import xml.XMLNode;

public class XGDeviceConfigurator implements ConfigurationConstants, XGComponent
{
	private XGDevice device;

	public XGDeviceConfigurator(XGDevice dev)
	{	if(dev == null) dev = new XGDevice(new XMLNode(TAG_DEVICE, null));
		this.device = dev;
	}

	@Override public JComponent getGuiComponent()
	{	XGFrame root = new XGFrame("device");
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

		root.add(this.device.getMidi().getGuiComponent());

		JSpinner sp = new JSpinner();
		sp.setAlignmentX(0.5f);
		sp.setAlignmentY(0.5f);
//			sp.setBorder(getDefaultBorder("sysex ID"));
		sp.setModel(new SpinnerNumberModel(this.device.getSysexID(), 0, 15, 1));
		sp.addChangeListener(new ChangeListener()
		{	@Override public void stateChanged(ChangeEvent e)
			{	JSpinner s = (JSpinner)e.getSource();
				this.device.setSysexID((int)s.getModel().getValue());
			}
		});
		root.add(sp);

		JButton btn = new JButton(this.device.getDefDumpPath().toString());
		btn.setAlignmentX(0.5f);
		btn.addActionListener(new ActionListener()
		{	@Override public void actionPerformed(ActionEvent e)
			{	XGSysexFile f = new XGSysexFile(null, this.device.getDefDumpPath().toString());
				Path p = f.selectPath(f.toString());
				this.device.setDefDumpPath(p);
				btn.setText(p.toString());
				btn.getTopLevelAncestor().revalidate();
			}
		});
		root.add(btn);

		return root;
	}

	@Override public void setForeground(Color col)
	{
	}

	@Override public void setBackground(Color col)
	{
	}
}
