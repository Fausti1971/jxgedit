package value;

import adress.XGIdentifiable;
import application.XGLoggable;import module.XGDrumsetModuleType;
import module.XGModuleType;
import table.XGDefaultsTable;
import xml.XMLNodeConstants;

public interface XGMutableDefaults extends XGIdentifiable
{
	XGValue getDefaultsSelector();

	void setDefaultsSelector(XGValue dsv);

	XGDefaultsTable getDefaultsTable();

	XGValue getThis();

	default int getDefaultValue()
	{	return this.getDefaultsTable().get(this.getID(), this.getDefaultsSelector().getValue());
	}

	default void initDefaultsDepencies()
	{	XGValue v = this.getThis();
		assert v.type != null;
		assert v.bulk != null;
		String dst = v.type.defaultSelectorTag;
		XGValue dsv = v.bulk.getModule().getValues().get(dst);

		if(dsv != null)
		{	this.setDefaultsSelector(dsv);
			this.getDefaultsSelector().valueListeners.add((XGValue)->v.setDefaultValue());
		}

		else if(XGValueType.DS_PRG_VALUE_TAG.equals(dst))
		{	XGModuleType t = v.getModule().getType();
			if(t instanceof XGDrumsetModuleType)
			{	this.setDefaultsSelector(((XGDrumsetModuleType)t).getProgramListener());
				this.getDefaultsSelector().valueListeners.add((XGValue)->v.setDefaultValue());
			}
		}

		else if(XGValueType.ID_VALUE_TAG.equals(dst))
		{	this.setDefaultsSelector(new XGFixedValue(v.getTag(), this.getID()));
		}

		else
		{	XGLoggable.LOG.warning(XMLNodeConstants.ATTR_DEFAULTSELECTOR + " " + dst + " not found for value " + v.getTag());
			this.setDefaultsSelector(XGFixedValue.DEF_DEFAULTSELECTOR);
		}
	}
}
