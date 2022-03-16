package adress;

public interface XGAddressConstants
{	
	public static final String REGEX_ADDRESS = "([\\d]+|[\\d]+-[\\d]+)?\\/([\\d]+|[\\d]+-[\\d]+)?\\/([\\d]+|[\\d]+-[\\d]+)?";

	public final int
		LSB = 0x7F,
		LSN = 0x0F,
		MSN = 0xF0;

	public final int DEF_MASK = 0x7F;
	public final XGAddressField DEF_ADDRESSFILED = new XGAddressField(0, 127, DEF_MASK);

	static final XGAddress
		XGALLADDRESS = new XGAddress(127, 127, 127),
		XGMODELNAMEADRESS = new XGAddress(1, 0, 0),
		XGMODELINFO1ADDRESS = new XGAddress(1, 0, 0x0E),
		XGMODELINFO2ADDRESS = new XGAddress(1, 0, 0x0F);
}
