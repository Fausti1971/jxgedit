package obj;

public interface XGObjectConstants
{	public static final int
		MASTER = 0x00,
		INFO = 0x01,
		SYSFX = 0x02,
		INSFX = 0x03,
		INSFXVL = 0x04,
		DTEXT = 0x06,
		DBITMAP = 0x07,
		MULTI = 0x08,
		MULTIADD = 0x0A,
		MULTIVL = 0x09,
		ADPART = 0x10,
		DRUM1 = 0x30,
		DRUM2 = 0x31,
		DRUM3 = 0x32,
		DRUM4 = 0x33;

	public static XGObject newXGObjectInstance(XGAdress adr)
	{	switch(adr.getHi())
		{	case 0x00:	return new XGObjectSystem(adr);
			case 0x01:	return new XGObjectInfo(adr);
			case 0x02:
				switch(adr.getMid())
				{	case 0x01:	return new XGObjectSysFX(adr);
					case 0x40:	return new XGObjectSysEQ(adr);
				}
			case 0x03:	return new XGObjectInsertFX(adr);
			case 0x04:	return new XGObjectInsertFXVL(adr);
			case 0x06:	return new XGObjectDisplayText(adr);
			case 0x07:	return new XGObjectDisplayBitmap(adr);
			case 0x08:
			case 0x0A:	return new XGObjectMultiPart(adr);
			case 0x09:	return new XGObjectMultiPartVL(adr);
			case 0x10:
			case 0x11:
			case 0x12:	return new XGObjectADPart(adr);
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
			case 0x3F:	return new XGObjectDrum(adr);
			case 0x70:
			case 0x71:	return new XGObjectPlugin(adr);
			default:	return new XGObjectUnknown(adr);
		}
	}
/*
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
*/}
