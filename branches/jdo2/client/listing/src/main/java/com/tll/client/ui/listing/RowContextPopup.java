package com.tll.client.ui.listing;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	private final IRowOptionsDelegate delegate;

	/**
	 * The needed table ref.
	 */
	private final ListingTable<?> table;

	/**
	 * The row index for this row context.
	 */
	private int rowIndex = -1;

	/**
	 * Constructor
	 * @param duration the time in mili-seconds to show the popup or
	 *        <code>-1</code> meaning it is shown indefinitely.
	 * @param table The table ref
	 * @param delegate the required row ops delegate
	 */
	public RowContextPopup(int duration, ListingTable<?> table, IRowOptionsDelegate delegate) {
		super(duration);
		if(table == null) throw new IllegalArgumentException("Null table ref");
		this.table = table;
		this.table.addClickHandler(this);
		if(delegate == null) throw new IllegalArgumentException("Null delegate");
		this.delegate = delegate;
	}

	public void onClick(ClickEvent event) {
		final Cell cell = table.getCellForEvent(event);
		final int row = cell.getRowIndex();
		Log.debug("RowContextPopup - onClick row: " + row);

		// account for header row and deleted rows
		if(row < 1 || table.isRowMarkedDeleted(row)) return;

		if(row != this.rowIndex) {
			this.rowIndex = row;
			setOptions(delegate.getOptions(row));
		}

		if(!isShowing()) {
			final NativeEvent ne = event.getNativeEvent();
			showAt(ne.getClientX(), ne.getClientY());
		}
	}

	@Override
	public void onOptionEvent(OptionEvent event) {
		if(delegate == null) throw new IllegalStateException("No row op delegate set");
		Log.debug("RowContextPopup - onOptionEvent event: " + event.toDebugString());
		super.onOptionEvent(event);
		if(event.getOptionEventType() == OptionEvent.EventType.SELECTED) {
			delegate.handleOptionSelection(event.optionText, rowIndex);
		}
	}

}