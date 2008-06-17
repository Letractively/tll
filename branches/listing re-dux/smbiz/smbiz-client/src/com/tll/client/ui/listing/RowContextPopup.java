package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.event.type.OptionEvent;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.ui.Option;
import com.tll.client.ui.OptionsPopup;

/**
 * RowContextPopup - The {@link Option}s panel pop-up.
 * @author jpk
 */
public final class RowContextPopup extends OptionsPopup implements TableListener {

	private static final int SHOW_DURATION = 2000; // 2s

	/**
	 * The bound {@link IRowOptionsDelegate}
	 */
	private IRowOptionsDelegate rowOpDelegate;

	/**
	 * The row index for this row context.
	 */
	private int rowIndex = -1;

	/**
	 * Constructor
	 */
	public RowContextPopup() {
		super(SHOW_DURATION);
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

	public final void onCellClicked(SourcesTableEvents sender, int row, int cell) {
		// assert sender == table.getTableWidget();

		// TODO account for deleted rows!!!

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
	public void onOptionSelected(OptionEvent event) {
		if(rowOpDelegate == null) throw new IllegalStateException("No row op delegate set");
		super.onOptionSelected(event);
		rowOpDelegate.handleOptionSelection(event.optionText, rowIndex);
	}

}