package msg;

import adress.XGAdress;

public interface XGBulkDump extends XGResponse
{	public XGAdress getAdress();
	public int getDumpSize();
	public int getBaseOffset();

}
