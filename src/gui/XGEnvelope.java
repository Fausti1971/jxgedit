package gui;

import adress.XGMemberNotFoundException;
import module.XGModule;
import parm.XGTable;
import xml.XMLNode;

public class XGEnvelope extends XGComponent
{
	private static final long serialVersionUID = 1L;

/**************************************************************************************/

	private int minX = 127, maxX = 0, minY = 127, maxY = 0, origin;
	private XGTable tableX = null, tableY = null;

	public XGEnvelope(XMLNode n, XGModule mod) throws XGMemberNotFoundException
	{	super(n, mod);
		for(XMLNode p : n.getChildNodes(TAG_POINT))
		{	this.add("point", new XGEnvelopePoint(this, p, mod));//TODE: XGComponent überschreibt add(Component c); vielleicht mal ändern...
		}
		this.borderize();

		LOG.info(this.getClass().getSimpleName() + " " + this.getName() + " initialized " + minX + "/" + maxX + "/" + minY + "/" + maxY);
	}

	public void setTableX(XGTable t)
	{	if(this.tableX == null)
		{	this.tableX = t;
			return;
		}
		if(!this.tableX.equals(t)) throw new RuntimeException("x-tables are not equal: " + this.tableX + "/" + t);
	}

	public void setTableY(XGTable t)
	{	if(this.tableY == null)
		{	this.tableY = t;
			return;
		}
		if(!this.tableY.equals(t)) throw new RuntimeException("y-tables are not equal: " + this.tableY + "/" + t);
	}

// Merke: hier mit echten Werten, anstatt Indexen arbeiten, da sonst die Grafik verfälscht wird...
	public int getMinX()
	{	return minX;
	}

	public void setMinX(int v)
	{	this.minX = Math.min(this.minX, v);
	}

	public int getMaxX()
	{	return maxX;
	}

	public void setMaxX(int v)
	{	this.maxX = Math.max(this.maxX, v);
	}

	public int getMinY()
	{	return minY;
	}

	public void setMinY(int v)
	{	this.minY = Math.min(this.minY, v);
	}

	public int getMaxY()
	{	return maxY;
	}

	public void setMaxY(int v)
	{	this.maxY = Math.max(this.maxY, v);
	}

	public int getOrigin()
	{	return origin;
	}

	public void setOrigin(int origin)
	{	this.origin = origin;
	}
}
