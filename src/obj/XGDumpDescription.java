package obj;
import application.InvalidXGAdressException;
public class XGDumpDescription implements XGObjectConstants
{	private final String name;
	private final XGAdress min, max;

	public XGDumpDescription(XGAdress min, XGAdress max, String n)
	{	this.min = min;
		this.max = max;
		this.name = n;
	}

	public XGDumpDescription(XGAdress min, XGAdress max) throws InvalidXGAdressException
	{	this(min, max, null);}

	public XGDumpDescription(XGAdress adr) throws InvalidXGAdressException
	{	this(adr, adr);}

	@Override public String toString()
	{	return "DS: " + name + " " + min + "..." + max;}
}