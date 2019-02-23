package obj;

public interface XGObjectConstants
{	public static Class<? extends XGObject> getXGObjectClass(XGAdress adr)
	{	switch(adr.getHi())
		{	case 0x00:	return XGObjectSystem.class;
			case 0x01:	return XGObjectInfo.class;
			case 0x02:
				switch(adr.getMid())
				{	case 0x01:	return XGObjectSysFX.class;
					case 0x40:	return XGObjectSysEQ.class;
				}
			case 0x03:	return XGObjectInsertFX.class;
			case 0x04:	return XGObjectInsertFXVL.class;
			case 0x06:	return XGObjectDisplayText.class;
			case 0x07:	return XGObjectDisplayBitmap.class;
			case 0x08:
			case 0x0A:	return XGObjectMultiPart.class;
			case 0x09:	return XGObjectMultiPartVL.class;
			case 0x10:
			case 0x11:
			case 0x12:	return XGObjectADPart.class;
			case 0x30:
			case 0x31:
			case 0x32:
			case 0x33:
			case 0x34:
			case 0x35:
			case 0x36:
			case 0x37:
			case 0x38:
			case 0x39:
			case 0x3A:
			case 0x3B:
			case 0x3C:
			case 0x3D:
			case 0x3E:
			case 0x3F:	return XGObjectDrum.class;
			case 0x70:
			case 0x71:	return XGObjectPlugin.class;
			default:	return XGObjectUnknown.class;
		}
	}

	public static XGObject getObjectInstance(XGAdress adr)
	{	switch(adr.getHi())
		{	case 0x00:	return XGObjectSystem.getInstance(adr);
			case 0x01:	return XGObjectInfo.getInstance(adr);
			case 0x02:
				switch(adr.getMid())
				{	case 0x01:	return XGObjectSysFX.getInstance(adr);
					case 0x40:	return XGObjectSysEQ.getInstance(adr);
				}
			case 0x03:	return XGObjectInsertFX.getInstance(adr);
			case 0x04:	return XGObjectInsertFXVL.getInstance(adr);
			case 0x06:	return XGObjectDisplayText.getInstance(adr);
			case 0x07:	return XGObjectDisplayBitmap.getInstance(adr);
			case 0x08:
			case 0x0A:	return XGObjectMultiPart.getInstance(adr);
			case 0x09:	return XGObjectMultiPartVL.getInstance(adr);
			case 0x10:
			case 0x11:
			case 0x12:	return XGObjectADPart.getInstance(adr);
			case 0x30:
			case 0x31:
			case 0x32:
			case 0x33:
			case 0x34:
			case 0x35:
			case 0x36:
			case 0x37:
			case 0x38:
			case 0x39:
			case 0x3A:
			case 0x3B:
			case 0x3C:
			case 0x3D:
			case 0x3E:
			case 0x3F:	return XGObjectDrum.getInstance(adr);
			case 0x70:
			case 0x71:	return XGObjectPlugin.getInstance(adr);
			default:	return XGObjectUnknown.getInstance(adr);
	}
	}
}
