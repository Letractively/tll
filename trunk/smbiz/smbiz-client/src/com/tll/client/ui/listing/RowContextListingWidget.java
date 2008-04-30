/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.event.IOptionListener;
import com.tll.client.event.IRowOptionListener;
import com.tll.client.event.type.OptionEvent;
import com.tll.client.event.type.RowOptionEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsProvider;
import com.tll.client.ui.Option;
import com.tll.client.ui.OptionsPopup;

/**
 * RowContextListingWidget - ListingWidget impl that supports row popup panels.
 * @author jpk
 */
public class RowContextListingWidget extends AbstractListingWidget implements IOptionListener {

	/**
	 * RowContextPopup - The {@link Option}s panel pop-up.
	 * @author jpk
	 */
	private final class RowContextPopup extends OptionsPopup implements TableListener {

		private static final int SHOW_DURATION = 2000; // 2s

		/**
		 * The bound {@link IRowOptionsProvider}
		 */
		private final IRowOptionsProvider optionProvider;

		/**
		 * The row index for this row context.
		 */
		private int rowIndex = -1;

		/**
		 * Constructor - Initially populates the options popup panel with the stock
		 * entries: row edit/delete based on the state of the listing configuration.
		 * @param rowOptionsProvider Provides the Options.
		 */
		public RowContextPopup(IRowOptionsProvider rowOptionsProvider) {
			super(SHOW_DURATION);

			assert rowOptionsProvider != null;
			if(rowOptionsProvider.isStaticOptions()) {
				setOptions(rowOptionsProvider.getOptions(null));
				this.optionProvider = null;
			}
			else {
				this.optionProvider = rowOptionsProvider;
			}
		}

		private void show(int row) {
			if(optionProvider != null) {
				setOptions(optionProvider.getOptions(table.getRowRef(row)));
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

		public int getRowIndex() {
			return rowIndex;
		}
	}

	/**
	 * The {@link RowContextPopup}.
	 */
	private final RowContextPopup rowContextPopup;

	/**
	 * The row event listener for event that are not row edit (update/delete)
	 * related.
	 */
	private final IRowOptionListener rowOptionListener;

	/**
	 * Constructor
	 * @param config The listing configuration
	 * @param rowOptionsProvider The row {@link Option}s provider. May be
	 *        <code>null</code>.
	 */
	public RowContextListingWidget(IListingConfig config, IRowOptionsProvider rowOptionsProvider) {
		super(config);

		if(rowOptionsProvider != null) {
			rowContextPopup = new RowContextPopup(rowOptionsProvider);
			rowContextPopup.addOptionListener(this);
			table.addTableListener(rowContextPopup);
			getListingPanel().addMouseListener(rowContextPopup);
		}
		else {
			rowContextPopup = null;
		}

		this.rowOptionListener = rowOptionsProvider.getRowOptionListener();

		getListingPanel().addKeyboardListener(table);
	}

	public void onCurrentOptionChanged(OptionEvent event) {
		// no-op
	}

	public void onOptionSelected(OptionEvent event) {
		if(rowOptionListener != null) {
			// fire the row option event
			rowContextPopup.hide();
			final String optionText = event.optionText;
			final int rowIndex = rowContextPopup.getRowIndex();
			rowOptionListener.onRowOptionSelected(new RowOptionEvent(this, optionText, rowIndex, table.getRowRef(rowIndex)));
		}
	}
}
