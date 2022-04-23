package parm;

import xml.XMLNodeConstants;

public interface XGParameterConstants extends XMLNodeConstants
{
	String NO_PARAMETERNAME = "no parameter";
	int  UNLIMITED = -1;
/**
 * ein Pseudo Selector-Value, damit auch der Parameter eines immutable-Opcodes im Set gespeichert werden kann (ebenso defaults)
 */
	int DEF_SELECTORVALUE = -1;
	int NO_PARAMETERVALUE = -1;

	XGParameter NO_PARAMETER = new XGParameter(NO_PARAMETERNAME, NO_PARAMETERVALUE);
}
