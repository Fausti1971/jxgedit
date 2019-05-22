package value;

import java.util.Map;
import java.util.TreeMap;

public interface XGValueStorage
{
	static Map<Integer, Map<Integer, Map<Integer, XGValue>>> STORAGE = new TreeMap<>();

}
