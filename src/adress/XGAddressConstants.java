package adress;

public interface XGAddressConstants
{	
	public final int
		MIDIBYTEMASK = 0x7F,
		INVALIDFIELD = -1;

	public final String
		TAG_HI = "hi",
		TAG_MID = "mid",
		TAG_LO = "lo";
	
	public final XGAddress INVALIDADRESS = new XGAddress(INVALIDFIELD, INVALIDFIELD, INVALIDFIELD);

	static final XGAddress
		XGMODELNAMEADRESS = new XGAddress(1, 0, 0),
		XGMODELINFO1ADDRESS = new XGAddress(1, 0, 0x0E),
		XGMODELINFO2ADDRESS = new XGAddress(1, 0, 0x0F);
}
