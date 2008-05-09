/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.event.type.OptionEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsManager;
import com.tll.client.ui.Option;
import com.tll.client.ui.OptionsPopup;

/**
 * RowContextListingWidget - ListingWidget impl that supports row popup panels.
 * @author jpk
 */
public class RowContextListingWidget extends AbstractListingWidget {

	/**
	 * RowContextPopup - The {@link Option}s panel pop-up.
	 * @author jpk
	 */
	private final class RowContextPopup extends OptionsPopup implements TableListener {

		private static final int SHOW_DURATION = 2000; // 2s

		/**
		 * The bound {@link IRowOptionsManager}
		 */
		private final IRowOptionsManager rowOpDelegate;

		/**
		 * The row index for this row context.
		 */
		private int rowIndex = -1;

		/**
		 * Constructor - Initially populates the options popup panel with the stock
		 * entries: row edit/delete based on the state of the listing configuration.
		 * @param rowOpDelegate Provides the Options.
		 */
		public RowContextPopup(IRowOptionsManager rowOpDelegate) {
			super(SHOW_DURATION);
			this.rowOpDelegate = rowOpDelegate;
		}

		private void show(int row) {
			if(rowOpDelegate != null) {
				setOptions(rowOpDelegate.getOptions(rowIndex, table.getRowRef(row)));
			}
			super.show();
		}

		public final void onCellClicked(SourcesTableEvents sender, int row, int cell) {
			assert sender == table.getTableWidget();

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
			rowOpDelegate.handleOptionSelection(event.optionText, rowIndex, table.getRowRef(rowIndex));
		}

	}

	/**
	 * The {@link RowContextPopup}.
	 */
	private final RowContextPopup rowContextPopup;

	/**
	 * Constructor
	 * @param config The listing configuration
	 * @param rowOptionsProvider The row {@link Option}s provider. May be
	 *        <code>null</code>.
	 */
	public RowContextListingWidget(IListingConfig config, IRowOptionsManager rowOptionsProvider) {
		super(config);

		if(rowOptionsProvider != null) {
			rowContextPopup = new RowContextPopup(rowOptionsProvider);
			table.addTableListener(rowContextPopup);
			getListingPanel().addMouseListener(rowContextPopup);
		}
		else {
			rowContextPopup = null;
		}

		getListingPanel().addKeyboardListener(table);
	}
}
