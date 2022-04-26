package adress;

public interface XGAddressable extends XGIdentifiable, XGAddressConstants
{
	XGAddressRange getAddress();

	default int getID()
	{	try
		{	return this.getAddress().getMid().getValue();
		}
		catch(InvalidXGAddressException e)
		{	System.out.println(e.getMessage());
			return this.getAddress().getMid().getMin();
		}
	}
}
