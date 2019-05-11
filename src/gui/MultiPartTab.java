package gui;

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import obj.XGAdress;
import obj.XGObject;
import obj.XGObjectConstants;
import parm.XGParameterConstants;

public class MultiPartTab extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

	public MultiPartTab()
	{//	Map<Integer, XGObject> map = XGObject.getXGObjectInstances(new XGAdress(XGObjectConstants.MULTI));
		int[] parms = {XGParameterConstants.MP_PARTMODE, XGParameterConstants.MP_PRG, XGParameterConstants.MP_CH, XGParameterConstants.MP_PAN}; 
		super.setLayout(new BorderLayout());
		super.add(new JScrollPane(new MultiPartListView()), BorderLayout.WEST);
//		super.add(new JScrollPane(new XGObjectTableView(XGObjectMultiPart.getTableModel()), BorderLayout.WEST);
		super.add(new MultiPartParameterTab(), BorderLayout.CENTER);
	}
}
