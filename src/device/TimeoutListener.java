package device;

public interface TimeoutListener
{
	void timeOut(long time) throws TimeoutException;
}

