package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextField;
import value.XGValue;

public class XGRangeLabel extends JTextField implements GuiConstants, ActionListener, MouseListener
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/*****************************************************************************************************/

	private final XGValue loValue, hiValue;

	public XGRangeLabel(XGValue lo, XGValue hi)
	{	super();
		this.loValue = lo;
		this.hiValue = hi;
		this.setBackground(COL_TRANSPARENT);
		this.setBorder(null);
//		if(this.isEnabled()) this.setText(this.getText());
		this.setText(this.loValue + "..." + this.hiValue);
		this.setFont(FONT);
		this.setHorizontalAlignment(JTextField.CENTER);
		this.addMouseListener(this);
		this.addActionListener(this);
	}

	@Override public void actionPerformed(ActionEvent e)
	{	boolean changed = this.loValue.setIndex(this.loValue.getParameter().getTranslationTable().getIndex(this.getText().trim()));
		if(changed) this.loValue.transmit();
		this.setText(this.loValue + "..." + this.hiValue);
//		this.repaint();
	}

	@Override protected void paintComponent(Graphics g)
	{	if(this.isEnabled()) super.paintComponent(g);
	}

	@Override public void mouseClicked(MouseEvent e)
	{	this.selectAll();
	}

	@Override public void mousePressed(MouseEvent e)
	{
	}

	@Override public void mouseReleased(MouseEvent e)
	{
	}

	@Override public void mouseEntered(MouseEvent e)
	{
	}

	@Override public void mouseExited(MouseEvent e)
	{
	}
}

