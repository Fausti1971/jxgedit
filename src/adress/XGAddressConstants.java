package adress;

public interface XGAddressConstants
{	
	String REGEX_ADDRESS = "([\\d]+|[\\d]+-[\\d]+)?\\/([\\d]+|[\\d]+-[\\d]+)?\\/([\\d]+|[\\d]+-[\\d]+)?";

	int
		LSB = 0x7F,
		LSN = 0x0F,
		MSN = 0xF0;

	int DEF_MASK = 0x7F;
	XGAddressField DEF_ADDRESSFILED = new XGAddressField(0, 127, DEF_MASK);

	XGAddress
		XGALLADDRESS = new XGAddress(127, 127, 127),
		XGMODELNAMEADRESS = new XGAddress(1, 0, 0),
		XGMODELINFO1ADDRESS = new XGAddress(1, 0, 0x0E),
		XGMODELINFO2ADDRESS = new XGAddress(1, 0, 0x0F);
}
