package module;

import java.util.Collections;
import java.util.Enumeration;
/*
 * config:
 * 
 * System:
 * 		bulklist;
 * 			opcodelist;
 * Info:
 * 		bulklist;
 * 			opcodelist;
 * System FX:
 * 		bulklist;
 * 			opcodelist;
 * SystemEQ:
 * 		bulklist;
 * 			opcodelist;
 * Insertion FX:
 * 		bulklist;
 * 			opcodelist;
 * 		children:	insertionfx;
 * Display:
 * 		bulklist;
 * 			opcodelist;
 * Multipart:
 * 		bulklist;
 * 			opcodelist;
 * 		children:	multiparts;
 * A/D-Part:
 * 		bulklist;
 * 		children:	adparts;
 * Drumset:
 * 		bulklist;
 * 			opcodelist;
 * 		children:	drumsets:
 * 			children:	drums;
 * Plugin:
 * 		bulklist;
 * 	
 */
import java.util.Set;
import javax.swing.tree.TreeNode;
import adress.InvalidXGAddressException;
import adress.XGAddress;
import adress.XGAddressable;
import gui.XGTreeNode;
import tag.XGTagable;

public interface XGModule extends XGAddressable, XGTagable, XGModuleConstants, XGTreeNode
{
	public static XGModuleTag getModuleTag(XGAddress adr)
	{	try
		{	switch(adr.getHi())
				{	case 0:		return XGModuleTag.system;
					case 1:		return XGModuleTag.info;
					case 2:
						switch(adr.getMid())
						{	case 1:		return XGModuleTag.sysfx;
							case 64:	return XGModuleTag.syseq;
						}
					case 3:		return XGModuleTag.insfx;
					case 6:
					case 7:		return XGModuleTag.display;
					case 8:
					case 9:
					case 10:	return XGModuleTag.multipart;
					case 16:
					case 17:
					case 18:	return XGModuleTag.adpart;
					case 48:
					case 49:
					case 50:
					case 51:	return XGModuleTag.drumset;
					case 112:
					case 113:	return XGModuleTag.plugin;
					default:	return XGModuleTag.unknown;
				}
		}
		catch(InvalidXGAddressException e)
		{	return XGModuleTag.unknown;
		}
	}

/********************************************************************************************************************/

	public Set<XGModule> getChildren();

	@Override public default boolean getAllowsChildren()
	{	return true;
	}

	@Override public default Enumeration<? extends TreeNode> children()
	{	return Collections.enumeration(this.getChildren());
	}


}