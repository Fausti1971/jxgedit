package gui;

import java.awt.Color;
import java.awt.Component;
import application.JXG;

public interface XGColorable extends XGDisplayable
{
	void setForeground(Color col);
	void setBackground(Color col);
	Component getParent();

	default Color getBaseColor()
	{	return new Color(JXG.config.getChildNodeOrNew(TAG_COLOR).parseChildNodeIntegerContentOrNew(TAG_BASECOLOR, Color.white.getRGB()));
	}
	
	default Color getFocusColor()
	{	return new Color(JXG.config.getChildNodeOrNew(TAG_COLOR).parseChildNodeIntegerContentOrNew(TAG_FOCUSCOLOR, Color.black.getRGB()));
	}

	default void colorize()
	{	Component p = this.getParent();
		if(p != null)
		{	this.setBackground(increase(p.getBackground()));
			this.setForeground(increase(p.getForeground()));
		}
		else
		{	this.setBackground(getBaseColor());
//			this.setForeground(increase(getBaseColor(), 16));
		}
	}

	default int getDepth(Component c)
	{	int i = 0;
		while(c.getParent() != null)
		{	i++;
			c = c.getParent();
		}
		return i;
	}

	default Color increase(Color base)
	{	return increase(base, 1);
	}

	default Color increase(Color base, int fac)
	{	int step = fac * COL_STEP;
		int r = base.getRed() + step;
		int g = base.getGreen() + step;
		int b = base.getBlue() + step;
		int a = base.getAlpha();
		return new Color(r, g, b, a);
	}

	default Color decrease(Color base)
	{	return decrease(base, 1);
	}

	default Color decrease(Color base, int fac)
	{	int step = fac * COL_STEP;
		int r = base.getRed() - step;
		int g = base.getGreen() - step;
		int b = base.getBlue() - step;
		int a = base.getAlpha();
		return new Color(r, g, b, a);
	}

}
