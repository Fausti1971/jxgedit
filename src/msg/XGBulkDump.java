package msg;

import adress.XGAddressable;

public interface XGBulkDump extends XGResponse, XGAddressable
{
	/**
	 * returniert die Anzahl der Nutzdaten im BulkDump, bspw. bei XGMessageBulkDump aus dem header
	 * @return
	 */
	public int getBulkSize();

	/**
	 * returniert das offset zu den Nutzdaten, bspw. bei XGMessageBulkDump das byte nach "address low"
	 * @return
	 */
	public int getBaseOffset();

	/**
	 * erstellt und returniert aus dem Inhalt aller im Bulk enthaltenen XGValues ein ByteArray der Größe "DumpSize" bzw. returniert die komplette XGMessageBulkDump an deren "BaseOffset" sich in der Größe "DumpSize" die Nutzdaten finden;
	 * @return
	 */
	public byte[] getBulkData();
}
