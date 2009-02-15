package com.tll.client.ui.listing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.option.OptionEvent;
import com.tll.client.ui.option.OptionsPopup;

/**
 * RowContextPopup - The {@link Option}s panel pop-up.
 * @author jpk
 */
public final class RowContextPopup extends OptionsPopup implements ClickHandler {

	private static final int SHOW_DURATION = 2000; // 2s

	/**
	 * The bound {@link IRowOptionsDelegate}
	 */
	private IRowOptionsDelegate rowOpDelegate;
	
	/**
	 * The needed table ref.
	 */
	private final HTMLTable table;

	/**
	 * The row index for this row context.
	 */
	private int rowIndex = -1;

	/**
	 * Constructor
	 * @param table The table ref
	 */
	public RowContextPopup(HTMLTable table) {
		super(SHOW_DURATION);
		this.table = table;
	}

	/**
	 * Sets or re-sets the row op delegate.
	 * @param rowOpDelegate The row op delgate to set. Can't be <code>null</code>.
	 */
	public void setDelegate(IRowOptionsDelegate rowOpDelegate) {
		if(rowOpDelegate == null) throw new IllegalArgumentException("A row op delegate must be specified");
		this.rowOpDelegate = rowOpDelegate;
	}

	private void show(int row) {
		if(rowOpDelegate == null) throw new IllegalStateException("No row op delegate set");
		setOptions(rowOpDelegate.getOptions(row));
		super.show();
	}

	public void onClick(ClickEvent event) {
		// assert sender == table.getTableWidget();

		// TODO account for deleted rows!!!
		final Cell cell = table.getCellForEvent(event);
		final int row = cell.getRowIndex();

		// account for header row
		if(row < 1) return;

		if(row != this.rowIndex) {
			hide();
			show(row);
		}
		else if(isShowing())
			hide();
		else
			show(row);

		this.rowIndex = row;
	}

	@Override
	public void onOptionEvent(OptionEvent event) {
		if(rowOpDelegate == null) throw new IllegalStateException("No row op delegate set");
		super.onOptionEvent(event);
		if(event.getOptionEventType() == OptionEvent.EventType.SELECTED) {
			rowOpDelegate.handleOptionSelection(event.optionText, rowIndex);
		}
	}

}