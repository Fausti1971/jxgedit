package value;

public interface ChangeableContent<T>
{
	public T getContent();

	public boolean setContent(T s);
}
