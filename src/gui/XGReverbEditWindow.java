package gui;
import module.XGModule;import javax.swing.*;

public class XGReverbEditWindow extends XGEditWindow
{
	public XGReverbEditWindow(XGModule mod)
	{	super(XGMainWindow.MAINWINDOW, mod);
		this.setContentPane(this.createContent());
		this.setVisible(true);
	}

	JComponent createContent()
	{	XGFrame root = new XGFrame();
		XGFrame main = new XGFrame(15,2);
		XGFrame parms = new XGFrame(15, 1);

		tag.XGTagableAddressableSet<value.XGValue> values = this.module.getValues();

		main.add(new gui.XGProgramSelector(values.get("rev_program")), "0,0,4,1");
		main.add(new gui.XGKnob(values.get("rev_return")), "4,0,1,2");
		main.add(new gui.XGKnob(values.get("rev_pan")), "5,0,1,2");
		root.add(main, "0,0,1,1");

		parms.add(new gui.XGKnob(values.get("rev_p1")), "0,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p2")), "1,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p3")), "2,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p4")), "3,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p5")), "4,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p6")), "5,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p7")), "6,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p8")), "7,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p9")), "8,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p10")), "9,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p11")), "10,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p12")), "11,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p13")), "12,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p14")), "13,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p15")), "14,0,1,1");
		parms.add(new gui.XGKnob(values.get("rev_p16")), "15,0,1,1");
		root.add(parms, "0,1,1,1");

		return root;
	}
}