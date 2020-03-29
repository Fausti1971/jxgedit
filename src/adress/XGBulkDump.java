package adress;
import opcode.XGOpcode;
import tag.XGTagableAddressableSet;
import xml.XMLNode;
import xml.XMLNodeConstants;

public class XGBulkDump extends XGAddress implements XMLNodeConstants
{
	public static final XGAddressableSet<XGBulkDump> init(XMLNode n)
	{
		XGAddressableSet<XGBulkDump> set = new XGAddressableSet<XGBulkDump>();

		for(XMLNode x : n.getChildNodes())
		{	if(x.getTag().equals(TAG_BULK))
			{	set.add(new XGBulkDump(x));
			}
		}
		return set;
	}

/******************************************************************************************************************/

	private final int size;
	private final XGTagableAddressableSet<XGOpcode> opcodes;

	public XGBulkDump(XMLNode n)
	{	super(n);
		this.size = n.getIntegerAttribute(ATTR_SIZE);
		this.opcodes = XGOpcode.init(n);
	}

	public int getSize()
	{	return this.size;
	}

	public XGTagableAddressableSet<XGOpcode> getOpcodes()
	{	return this.opcodes;
	}
}