package adress;

public interface XGAddressable extends XGAddressConstants
{
/**
 * zum Sortieren und Iterieren erforderliche XGAdress eines XGAdressable
 * @return XGAddress
 * @throws InvalidXGAddressException 
 */
	public XGAddress getAddress();
}
