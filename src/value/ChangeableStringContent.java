package value;

public class ChangeableStringContent implements ChangeableContent<String>
{
	private String content;

	public ChangeableStringContent(final String cnt)
	{	this.content = cnt;
	}

	@Override public String getContent()
	{	return this.content;
	}

	@Override public boolean setContent(final String s)
	{	boolean changed = this.content.equals(s);
		this.content = s;
		return changed;
	}
}
