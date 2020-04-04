package adress;

public interface XGAddressConstants
{	
	public enum XGAddressModulator
	{	
	}
	
	public final int
		LSB = 0x7F,
		LSN = 0x0F,
		MSN = 0xF0;

	public final String
		ATTR_HI = "hi",
		ATTR_MID = "mid",
		ATTR_LO = "lo";
	
	public final int DEF_MASK = 0x7F;
	public final XGAddressField DEF_ADDRESSFILED = new XGAddressField(0, 127, DEF_MASK);

	static final XGAddress
		XGALLADDRESS = new XGAddress(DEF_ADDRESSFILED, DEF_ADDRESSFILED, DEF_ADDRESSFILED),
		XGMODELNAMEADRESS = new XGAddress(1, 0, 0),
		XGMODELINFO1ADDRESS = new XGAddress(1, 0, 0x0E),
		XGMODELINFO2ADDRESS = new XGAddress(1, 0, 0x0F);
}
