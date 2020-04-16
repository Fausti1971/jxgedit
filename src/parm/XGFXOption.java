package parm;

public class XGFXOption
{

/*************************************************************************************************************/

	final boolean xg_standard, xg_option, xglite_standard, xglite_option;

	public XGFXOption(String s)
	{	switch(s)
		{	case "+":	xg_standard = false;
						xg_option = true;
						xglite_standard = false;
						xglite_option = true;
						break;
			case "-":	xg_standard = true;
						xg_option = false;
						xglite_standard = false;
						xglite_option = true;
						break;
			case " ":	xg_standard = true;
						xg_option = false;
						xglite_standard = true;
						xglite_option = false;
						break;
			default:	xg_standard = false;
						xg_option = false;
						xglite_standard = false;
						xglite_option = false;
		}
	}
}
