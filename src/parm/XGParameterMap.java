package parm;

import java.util.HashMap;
import java.util.Map;

public class XGParameterMap implements XGParameterConstants
{	private static Map<Integer, XGParameterMap> parameterMaps = new HashMap<>();	//FX-Programm-basierte MapMap (Reverb1, Reverb2, ...)

	private static XGParameterMap getParameterMap(int prg)
	{	return parameterMaps.get(prg);}

	public static XGParameter getParameter(int prg, int index)
	{	return getParameterMap(prg).getParameter(index);}

/************************************************************************************/

	private Map<Integer, XGParameter> parameterMap = new HashMap<>();	//Parameter-Tag-basierte ParameterMap (P1, P2, P3...)

	public XGParameterMap(int... prgs)
	{	for(int p : prgs) parameterMaps.put(p, this);
	}

	public void addParameter(int index, XGParameter p)
	{	parameterMap.put(index, p);}

	private XGParameter getParameter(int index)
	{	return parameterMap.get(index);}
	
}
