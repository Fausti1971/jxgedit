package adress;

public interface XGAddressConstants
{	
	public enum XGAddressModulator
	{	
	}
	
	public final int
		MIDIBYTEMASK = 0x7F;

	public final String
		ATTR_HI = "hi",
		ATTR_MID = "mid",
		ATTR_LO = "lo";
	
	public final XGAddressField DEF_ADDRESSFILED = new XGAddressField(0, 127);

	static final XGAddress
		XGALLADDRESS = new XGAddress(DEF_ADDRESSFILED, DEF_ADDRESSFILED, DEF_ADDRESSFILED),
		XGMODELNAMEADRESS = new XGAddress(1, 0, 0),
		XGMODELINFO1ADDRESS = new XGAddress(1, 0, 0x0E),
		XGMODELINFO2ADDRESS = new XGAddress(1, 0, 0x0F);
}
