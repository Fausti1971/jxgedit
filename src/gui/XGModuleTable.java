package gui;
import static application.XGLoggable.LOG;import device.XGMidi;import module.*;import value.XGValue;
import javax.sound.midi.InvalidMidiDataException;import javax.sound.midi.ShortMessage;import javax.swing.*;
import javax.swing.event.ListSelectionEvent;import javax.swing.table.*;
import java.awt.*;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;

public class XGModuleTable extends JTable implements MouseListener
{
	private static final int GAP = 12, MIDI_VELOCITY = 100;
	private static final JButton DEF_BUTTON = new JButton();
	private static final JLabel DEF_LABEL = new JLabel();
	private static ShortMessage PLAYING_NOTE;
	static
	{	DEF_LABEL.setHorizontalAlignment(JLabel.CENTER);
		DEF_LABEL.setOpaque(true);
	}
	private static final TableCellRenderer DEF_LABEL_RENDERER = (table,o,isSelected,hasFocus,row,col)->
	{	if(o instanceof XGValue)
		{	XGValue v = (XGValue)o;
			String s = v.toString();

			Color bg = XGUI.COL_BAR_BACK;
			if(v.getValue() != v.getDefaultValue()) bg = XGUI.COL_BAR_BACK_CHANGED;
			if(isSelected) bg = XGUI.COL_BAR_BACK_DARK;
			DEF_LABEL.setBackground(bg);

			DEF_LABEL.setText(s);

			XGModuleTable t = (XGModuleTable)table;
			FontMetrics fm = DEF_LABEL.getFontMetrics(DEF_LABEL.getFont());
			int w = SwingUtilities.computeStringWidth(fm, s);
			t.getColumnModel().getColumn(col).setMinWidth(w);
		}
		return DEF_LABEL;
	};

	private static final TableCellRenderer DEF_BUTTON_RENDERER = (table,o,isSelected,hasFocus,row,col)->
	{	if(o instanceof XGModule)
		{	String s = o.toString();

			DEF_BUTTON.setText(s);

			XGModuleTable t = (XGModuleTable)table;
			FontMetrics fm = DEF_BUTTON.getFontMetrics(DEF_BUTTON.getFont());
			int w = SwingUtilities.computeStringWidth(fm, s);
			t.getColumnModel().getColumn(col).setMinWidth(w);
		}
		return DEF_BUTTON;
	};

/***********************************************************************************************************************/

	public XGModuleTable(XGModuleType type)
	{	super(new XGModuleTableModel(type));
		this.getModel().addTableModelListener(this);
		this.setCellSelectionEnabled(true);
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.getSelectionModel().addListSelectionListener(this);
		this.setAutoCreateRowSorter(true);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.setRowHeight(this.getFont().getSize() + GAP);
		this.setShowGrid(true);//wird vom L&F wieder überschrieben
		this.setAutoscrolls(true);
		this.setDefaultRenderer(XGModule.class, (JTable table,Object o,boolean b,boolean b1,int i,int i1)->{ DEF_BUTTON.setText(o.toString()); return DEF_BUTTON;});
		this.setDefaultRenderer(XGValue.class, DEF_LABEL_RENDERER);
		this.setDefaultRenderer(XGModule.class, DEF_BUTTON_RENDERER);
		((DefaultTableCellRenderer)this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);//wird von Nimbus und GTK+ überschrieben
		if(type instanceof XGDrumsetModuleType) this.addMouseListener(this);
	}

	@Override public void valueChanged(ListSelectionEvent e)//ListSelectionListener
	{	super.valueChanged(e);
		if(e.getValueIsAdjusting()) return;
		String valueString = null;
		boolean answered = false;

		for(int col : this.getSelectedColumns())
		{	for(int r : this.getSelectedRows())
			{	int selRow = this.getRowSorter().convertRowIndexToModel(r);
				Object o = this.getModel().getValueAt(selRow, col);
				if(o instanceof XGModule)
				{	XGEditWindow.getEditWindow((XGModule)o).setVisible(true);
					this.clearSelection();
					continue;
				}
				if(o instanceof XGValue)
				{	if(!answered)
					{	valueString = JOptionPane.showInputDialog(this, this.getColumnName(col), "0");
						answered = true;
						if(valueString == null)
						{	this.clearSelection();
							return;
						}
					}
					try
					{	((XGValue)o).setValue(valueString, true);
					}
					catch(NumberFormatException ex)
					{	LOG.warning(ex.getMessage());
					}
				}
			}
		}
		this.clearSelection();
	}

	@Override public void mouseClicked(MouseEvent event)
	{
	}

	@Override public void mousePressed(MouseEvent event)
	{	if(event.isPopupTrigger())
		{	this.playNoteOff();
			Object o = this.getValueAt(this.rowAtPoint(event.getPoint()), this.columnAtPoint(event.getPoint()));
			XGModule m = null;
			XGDrumsetModuleType dsmt;
			if(o instanceof XGModule)
			{	m = (XGModule)o;
				if(m.getType() instanceof XGDrumsetModuleType)
				{	dsmt = (XGDrumsetModuleType)m.getType();
					int ch = dsmt.getMidiChannel();
					if(ch != -1)
					{	try
						{	PLAYING_NOTE = new ShortMessage(0x90 | (ch % 0xF), m.getID(), MIDI_VELOCITY);
							XGMidi.getMidi().transmit(PLAYING_NOTE);
						}
						catch(InvalidMidiDataException e)
						{	e.printStackTrace();
						}
					}
				}
			}
		}
		//LOG.info("mousePressed=" + event);
	}

	private void playNoteOff()
	{	if(PLAYING_NOTE != null)
		{	byte[] array = PLAYING_NOTE.getMessage();
			try
			{	XGMidi.getMidi().transmit(new ShortMessage(array[0], array[1], 0));
			}
			catch(InvalidMidiDataException e)
			{	e.printStackTrace();
			}
			PLAYING_NOTE = null;
		}
	}

	@Override public void mouseReleased(MouseEvent event)
	{	this.playNoteOff();
	}

	@Override public void mouseEntered(MouseEvent event)
	{
	}

	@Override public void mouseExited(MouseEvent event)
	{
	}
}