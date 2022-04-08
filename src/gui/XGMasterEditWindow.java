package gui;

import device.XGDevice;
import module.XGDrumsetModuleType;
import module.XGModuleType;
import static module.XGModuleType.TYPES;
import tag.XGTagableAddressableSet;
import value.XGValue;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class XGMasterEditWindow extends XGEditWindow 
{
/**********************************************************************************************************/

	public XGMasterEditWindow(module.XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setVisible(true);
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame(false);

		XGTagableAddressableSet<XGValue> values = this.module.getValues();

		int y = 0;
		root.add(new XGKnob(values.get("master_volume")), new int[]{0,y,1,2});
		root.add(new XGKnob(values.get("master_damp")), new int[]{1,y,1,2});
		root.add(new XGKnob(values.get("master_transpose")), new int[]{2,y,1,2});
		root.add(new XGKnob(values.get("master_tune")), new int[]{3,y,1,2});

		JButton b;
		y = 2;
		for(XGModuleType mt : TYPES)
		{	if(mt instanceof XGDrumsetModuleType)
			{	b = new JButton("Reset " + mt.getName());
				b.addActionListener((ActionEvent e)->((XGDrumsetModuleType)mt).reset());
				root.add(b, new int[]{0,y++,4,1});
			}
		}
		b = new JButton("XG System On");
		b.addActionListener((ActionEvent e)->XGDevice.device.resetXG(true, true));
		root.add(b, new int[]{0,y++,4,1});

		b = new JButton("Reset All Parameters");
		b.addActionListener((ActionEvent e)->XGDevice.device.resetAll(true, true));
		root.add(b, new int[]{0,y++,4,1});

		return root;
	}
}
