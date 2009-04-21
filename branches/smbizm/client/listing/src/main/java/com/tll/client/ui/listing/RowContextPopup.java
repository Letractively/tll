package com.tll.client.ui.listing;

import com.google.gwt.dom.client.NativeEvent;
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
		this(DFLT_DURATION, table);
	}

	/**
	 * Constructor
	 * @param duration the time in mili-seconds to show the popup or
	 *        <code>-1</code> meaning it is shown indefinitely.
	 * @param table The table ref
	 */
	public RowContextPopup(int duration, HTMLTable table) {
		super(duration);
		if(table == null) throw new IllegalArgumentException("Null table ref");
		this.table = table;
		this.table.addClickHandler(this);
	}

	/**
	 * Sets or re-sets the row op delegate.
	 * @param rowOpDelegate The row op delgate to set. Can't be <code>null</code>.
	 */
	public void setDelegate(IRowOptionsDelegate rowOpDelegate) {
		if(rowOpDelegate == null) throw new IllegalArgumentException("A row op delegate must be specified");
		this.rowOpDelegate = rowOpDelegate;
	}

	public void onClick(ClickEvent event) {
		// assert sender == table.getTableWidget();

		// TODO account for deleted rows!!!
		final Cell cell = table.getCellForEvent(event);
		final int row = cell.getRowIndex();

		// account for header row
		if(row < 1) return;

		if(row != this.rowIndex) {
			this.rowIndex = row;
			setOptions(rowOpDelegate.getOptions(row));
		}

		if(!isShowing()) {
			final NativeEvent ne = event.getNativeEvent();
			showAt(ne.getClientX(), ne.getClientY());
		}
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