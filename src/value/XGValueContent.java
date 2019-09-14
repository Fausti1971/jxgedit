package value;

public class XGValueContent<T>
{	private T content;

	public XGValueContent(T obj)
	{	this.content = obj;
		System.out.println(this.content.getClass().getName());
	}

	public T getContent()
	{	return this.content;}

	public void setContent(T obj)
	{	this.content = obj;}
}
