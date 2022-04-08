package value;

public interface ChangeableContent<T>
{
	T getContent();

	boolean setContent(T s);
}
