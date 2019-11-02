package opcode;

public class NoSuchOpcodeException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID=1L;

/************************************************************************************************/

	public NoSuchOpcodeException(String string)
	{	super("no such opcode " + string);
	}

}
