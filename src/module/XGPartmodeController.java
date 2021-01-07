package module;

import java.util.HashMap;
import java.util.Map;
import device.XGDevice;

public class XGPartmodeController
{	private static final int DEF_DRUMSETPROGRAM = 127 << 14;

/*************************************************************************************************************/

	private final XGDevice device;
	private final Map<Integer, Integer> normalPrograms = new HashMap<>();//mp_id (partmode = 0), prog
	private final Map<Integer, Integer> drumKitPrograms = new HashMap<>();//mp-id (partmode = 1), prog
	private final Map<Integer, XGDrumsetModuleType> drumsets = new HashMap<>();//partmode (> 1), XGDrumsetModuleType

	public XGPartmodeController(XGDevice dev)
	{	this.device = dev;
	}
}
