package application;

import midi.XGDevice;
import obj.XGObject;

public interface ChangeListener
{	void deviceChanged(XGDevice d);
	void deviceAdded(XGDevice d);
	void objChanged(XGObject o);
	void objAdded(XGObject o);
}
