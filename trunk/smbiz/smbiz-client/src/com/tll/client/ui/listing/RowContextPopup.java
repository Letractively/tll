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
	private final IRowOptionsDelegate rowOpDelegate;

	/**
	 * The row index for this row context.
	 */
	private int rowIndex = -1;

	/**
	 * Constructor - Initially populates the options popup panel with the stock
	 * entries: row edit/delete based on the state of the listing configuration.
	 * @param rowOpDelegate Provides the Options.
	 */
	public RowContextPopup(IRowOptionsDelegate rowOpDelegate) {
		super(SHOW_DURATION);
		assert rowOpDelegate != null;
		this.rowOpDelegate = rowOpDelegate;
	}

	private void show(int row) {
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
		super.onOptionSelected(event);
		assert rowOpDelegate != null;
		rowOpDelegate.handleOptionSelection(event.optionText, rowIndex);
	}

}