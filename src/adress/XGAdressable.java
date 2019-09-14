package adress;

public interface XGAdressable extends XGAdressConstants
{
/**
 * zum Sortieren und Iterieren erforderliche XGAdress eines XGAdressable
 * @return
 */
public XGAdress getAdress();

/**
 * eine informative Zusammenstellung über ein XGAdressable zur Darstellung
 * @return
 */
	public String getInfo();
}
