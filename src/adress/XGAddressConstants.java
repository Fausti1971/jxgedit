package adress;

public interface XGAddressConstants
{	
	public enum XGAddressModulator
	{	
	}
	
	public final int
		MIDIBYTEMASK = 0x7F,
		INVALIDFIELD = -1;

	public final String
		ATTR_HI = "hi",
		ATTR_MID = "mid",
		ATTR_LO = "lo";
	
	public final XGAddress INVALIDADRESS = new XGAddress(INVALIDFIELD, INVALIDFIELD, INVALIDFIELD);

	static final XGAddress
		XGMODELNAMEADRESS = new XGAddress(1, 0, 0),
		XGMODELINFO1ADDRESS = new XGAddress(1, 0, 0x0E),
		XGMODELINFO2ADDRESS = new XGAddress(1, 0, 0x0F);
}
