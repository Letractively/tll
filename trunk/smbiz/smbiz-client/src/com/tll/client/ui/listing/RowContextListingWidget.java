/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.App;
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

	private static final String EDIT_OPTION_PREFIX = "Edit ";
	private static final String DELETE_OPTION_PREFIX = "Delete ";

	/**
	 * Generates an edit option with the given name.
	 * <p>
	 * FORMAT: "Edit {subjectName}"
	 * @param subjectName
	 * @return New Option instance
	 */
	public static Option editOption(String subjectName) {
		return new Option(EDIT_OPTION_PREFIX + subjectName, App.imgs().pencil().createImage());
	}

	/**
	 * Generates a delete option with the given name.
	 * <p>
	 * FORMAT: "Delete {subjectName}"
	 * @param subjectName
	 * @return New Option instance
	 */
	public static Option deleteOption(String subjectName) {
		return new Option(DELETE_OPTION_PREFIX + subjectName, App.imgs().trash().createImage());
	}

	/**
	 * Does the option text indicate edit?
	 * @param optionText
	 * @return true/false
	 */
	public static boolean isEditOption(String optionText) {
		return optionText == null ? false : optionText.startsWith(EDIT_OPTION_PREFIX);
	}

	/**
	 * Does the option text indicate delete?
	 * @param optionText
	 * @return true/false
	 */
	public static boolean isDeleteOption(String optionText) {
		return optionText == null ? false : optionText.startsWith(DELETE_OPTION_PREFIX);
	}

	/**
	 * RowContextPopup - The {@link Option}s panel pop-up.
	 * @author jpk
	 */
	private final class RowContextPopup extends OptionsPopup implements TableListener {

		private static final int SHOW_DURATION = 2000; // 2s

		/**
		 * The bound {@link IRowOptionsManager}
		 */
		private final IRowOptionsManager optionProvider;

		/**
		 * The row index for this row context.
		 */
		private int rowIndex = -1;

		/**
		 * Constructor - Initially populates the options popup panel with the stock
		 * entries: row edit/delete based on the state of the listing configuration.
		 * @param rowOptionsProvider Provides the Options.
		 */
		public RowContextPopup(IRowOptionsManager rowOptionsProvider) {
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

		@Override
		public void onOptionSelected(OptionEvent event) {
			super.onOptionSelected(event);
			optionProvider.handleOptionEvent(event, table.getRowRef(rowIndex));
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
