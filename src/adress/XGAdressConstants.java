package adress;

public interface XGAdressConstants
{	public final int MIDIBYTEMASK = 0x7F, INVALIDFIELD = -1;
	public enum AdressFieldType{HI, MID, LO};
	String
		TAG_HI = "hi",
		TAG_MID = "mid",
		TAG_LO = "lo";
	
	public final XGAdress INVALIDADRESS = new XGAdress(INVALIDFIELD, INVALIDFIELD, INVALIDFIELD);
}
