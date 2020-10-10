package gui;

import adress.XGAddress;
import module.XGModule;
import xml.XMLNode;

public class XGMEQ extends XGComponent implements GuiConstants
{
	private static final long serialVersionUID = 1L;
	private static final XGAddress
		G1 = new XGAddress("2/64/1"),
		G2 = new XGAddress("2/64/5"),
		G3 = new XGAddress("2/64/9"),
		G4 = new XGAddress("2/64/13"),
		G5 = new XGAddress("2/64/17"),
		F1 = new XGAddress("2/64/2"),
		F2 = new XGAddress("2/64/6"),
		F3 = new XGAddress("2/64/10"),
		F4 = new XGAddress("2/64/14"),
		F5 = new XGAddress("2/64/18"),
		Q1 = new XGAddress("2/64/3"),
		Q2 = new XGAddress("2/64/6"),
		Q3 = new XGAddress("2/64/11"),
		Q4 = new XGAddress("2/64/15"),
		Q5 = new XGAddress("2/64/19"),
		S1 = new XGAddress("2/64/4"),
		S5 = new XGAddress("2/64/20");

/*****************************************************************************************/

	private final XGDrawPanel panel;

	public XGMEQ(XMLNode n, XGModule mod)
	{	super(n, mod);
		this.borderize();
		this.setLayout(null);

		this.panel = new XGDrawPanel(this, n);

		this.add(this.panel);
	}
}
