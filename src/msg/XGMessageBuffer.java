package msg;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import adress.InvalidXGAddressException;
import adress.XGAddressableSet;
import application.XGLoggable;
import device.XGDevice;
import gui.XGFrame;
import gui.XGWindow;
import gui.XGWindowSource;

public class XGMessageBuffer extends XGAddressableSet<XGMessage> implements XGMessenger, XGWindowSource, XGLoggable
{	private final XGMessenger source;
	private XGWindow window;
	private final JList<XGMessage> list = new JList<>(new DefaultListModel<XGMessage>());
	private JLabel status = new JLabel();
//	private XMLNode config = new XMLNode("buffer", null);

	public XGMessageBuffer(XGMessenger src)
	{	this.source = src;
	}

	@Override public XGDevice getDevice()
	{	return this.source.getDevice();
	}

	@Override public String getMessengerName()
	{	return this.source.getMessengerName() + " buffer";
	}

	@Override public void submit(XGMessage m)
	{	this.add(m);
		LOG.info("msg buffered: " + m);
		((DefaultListModel<XGMessage>)this.list.getModel()).addElement(m);
		if(this.window == null) this.setChildWindow(new XGWindow(this, XGWindow.getRootWindow(), false, this.getMessengerName()));
		this.status.setText(this.size() + " messages buffered");

		if(this.window.isVisible())
		{	this.window.setPreferredSize(new Dimension(400, (int)this.list.getSize().getHeight()));
			this.window.pack();
		}
		else
		{	this.window.pack();
			this.window.setVisible(true);
		}
	}

	private void rm(XGMessage m)
	{	super.remove(m);
		((DefaultListModel<XGMessage>)this.list.getModel()).removeElement(m);
		this.status.setText(this.size() + " messages buffered");
		this.window.setPreferredSize(new Dimension(400, (int)this.list.getSize().getHeight()));
		this.window.pack();
		if(this.size() == 0) this.window.dispose();
	}

	@Override public void windowOpened(WindowEvent e)
	{
	}

	@Override public void windowClosing(WindowEvent e)
	{	((DefaultListModel<XGMessage>)this.list.getModel()).clear();
		this.clear();
	}

	@Override public void windowClosed(WindowEvent e)
	{	this.window = null;
	}

	@Override public void windowIconified(WindowEvent e)
	{
	}

	@Override public void windowDeiconified(WindowEvent e)
	{
	}

	@Override public void windowActivated(WindowEvent e)
	{
	}

	@Override public void windowDeactivated(WindowEvent e)
	{
	}

	@Override public XGWindow getChildWindow()
	{	return this.window;
	}

	@Override public void setChildWindow(XGWindow win)
	{	this.window = win;
	}

	@Override public JComponent getChildWindowContent()
	{	JComponent root = new XGFrame(this.getMessengerName());
//		root.setLayout(new BorderLayout());
		root.add(new JScrollPane(this.list));

		JToolBar tb = new JToolBar(JToolBar.HORIZONTAL);
		tb.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		tb.setFloatable(false);

		JButton b = new JButton("delete selected");
		b.addActionListener(new AbstractAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e)
			{	for(XGMessage m : list.getSelectedValuesList()) rm(m);
			}
		});
		tb.add(b);

		b = new JButton("selected to " + this.source.getMessengerName());
		b.addActionListener(new AbstractAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e)
			{	for(XGMessage m : list.getSelectedValuesList())
				{	m.setDestination(source);
					if(m instanceof XGResponse) try
					{
						source.submit(m);
					}
					catch(InvalidXGAddressException | XGMessengerException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rm(m);
				}
			}
		});
		tb.add(b);
		tb.validate();

		root.add(tb);

		this.status.setText(this.size() + " messages buffered");
		this.status.setHorizontalAlignment(SwingConstants.CENTER);
		this.status.setVerticalAlignment(SwingConstants.CENTER);
		root.add(this.status);

		return root;
	}

	@Override public void request(XGRequest req) throws InvalidXGAddressException
	{
	}

	@Override public void close()
	{
		// TODO Auto-generated method stub
		
	}
}
