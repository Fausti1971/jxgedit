package application;

import java.util.Vector;

public class NamedVector<E extends Object> extends Vector<E>
{	/**
	 * 
	 */
	private static final long serialVersionUID=4837218592870592762L;

/**************************************************************************/

	private String name;

	public String getName()
	{	return name;
	}

	public void setName(String name)
	{	this.name = name;
	}
}
