/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.event.type.OptionEvent;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.model.IData;
import com.tll.client.ui.Option;
import com.tll.client.ui.OptionsPopup;

/**
 * RowContextListingWidget - ListingWidget impl that supports row popup panels.
 * @author jpk
 */
public class RowContextListingWidget<R extends IData> extends ListingWidget<R> {

	/**
	 * RowContextPopup - The {@link Option}s panel pop-up.
	 * @author jpk
	 */
	private final class RowContextPopup extends OptionsPopup implements TableListener {

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
			setOptions(rowOpDelegate.getOptions(row, table.getRowRef(row)));
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
	 * @param rowOptionsDelegate The row {@link Option}s provider. May be
	 *        <code>null</code>.
	 * @param addRowDelegate The delegate to handle adding row requests.
	 */
	public RowContextListingWidget(IListingConfig<R> config, IRowOptionsDelegate rowOptionsDelegate,
			IAddRowDelegate addRowDelegate) {
		super(config, addRowDelegate);

		if(rowOptionsDelegate != null) {
			rowContextPopup = new RowContextPopup(rowOptionsDelegate);
			table.addTableListener(rowContextPopup);
			getListingPanel().addMouseListener(rowContextPopup);
		}
		else {
			rowContextPopup = null;
		}

		getListingPanel().addKeyboardListener(table);
	}
}
