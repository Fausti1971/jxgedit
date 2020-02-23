package msg;

public interface XGRequest extends XGMessage
{	
	boolean setResponsedBy(XGResponse msg);
	boolean isResponsed();
	XGResponse getResponse();
}
