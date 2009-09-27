/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingHandler;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingEvent;
import com.tll.client.ui.SimpleHyperLink;
import com.tll.dao.SortColumn;
import com.tll.dao.SortDir;
import com.tll.dao.Sorting;

/**
 * ListingTable - ListingWidget specific HTML table.
 * @author jpk
 * @param <R>
 */
public class ListingTable<R> extends Grid implements ClickHandler, KeyDownHandler, IListingHandler<R> {

	/**
	 * The listing table specific image bundle.
	 */
	private static final ListingTableImageBundle imageBundle =
		(ListingTableImageBundle) GWT.create(ListingTableImageBundle.class);

	/**
	 * Styles - (tableview.css)
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * The actual HTML table tag containing the listing data gets this style
		 * (Style class).
		 */
		public static final String TABLE = "table";
		public static final String SORT = "sort";
		public static final String COUNT_COL = "countCol";
		public static final String HEAD = "head";
		public static final String EVEN = "even";
		public static final String ODD = "odd";

		public static final String ADDED = "added";
		public static final String UPDATED = "updated";
		public static final String DELETED = "deleted";

		public static final String CURRENT = "crnt";
		public static final String ACTIVE = "actv";

	} // Styles

	/**
	 * The sort column arrow.
	 */
	private Image imgSortDir;

	protected Column[] columns;

	protected boolean ignoreCaseWhenSorting;

	protected ITableCellRenderer<R> cellRenderer;

	protected IListingOperator<R> listingOperator;

	/**
	 * The column index holding the row num. -1 indicates the row num col doesn't
	 * exist.
	 */
	protected int rowNumColIndex;

	/**
	 * The column index of the currently sorted column.
	 */
	private int crntSortColIndex = -1;

	/**
	 * Associates a Column to a column header cell widget in the header row. Used
	 * only for sorting.
	 */
	private SortLink[] sortlinks;

	/**
	 * The currently "active" table row index (dictated by mouse hover).
	 */
	private int actvRowIndex = -1;

	/**
	 * The currently selected table row index.
	 */
	private int crntRowIndex = -1;

	/**
	 * The current calculated 1-based page number.
	 */
	private int crntPage = -1;

	/**
	 * The calculated number of listing pages.
	 */
	private int numPages = 0;

	/**
	 * Constructor
	 * @param config
	 */
	public ListingTable(IListingConfig<R> config) {
		super();
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		addClickHandler(this);
		addHandler(this, KeyDownEvent.getType());
		initialize(config);
	}

	/**
	 * Initializes the table.
	 * @param config
	 */
	@SuppressWarnings("unchecked")
	protected void initialize(IListingConfig config) {
		assert config != null;

		this.columns = config.getColumns();
		this.cellRenderer = config.getCellRenderer();
		assert columns != null && cellRenderer != null;

		this.ignoreCaseWhenSorting = config.isIgnoreCaseWhenSorting();

		int rn = -1;
		final Column[] clmns = config.getColumns();
		for(int i = 0; i < clmns.length; i++) {
			if(Column.ROW_COUNT_COLUMN == clmns[i]) {
				rn = i;
				break;
			}
		}
		rowNumColIndex = rn;

		if(config.isSortable()) {
			sortlinks = new ListingTable.SortLink[clmns.length];
		}

		setStyleName(Styles.TABLE);

		addHeaderRow(config);
	}

	/**
	 * Sets the listing operator on behalf of the containing listing Widget.
	 * @param listingOperator The listing operator
	 */
	final void setListingOperator(IListingOperator<R> listingOperator) {
		this.listingOperator = listingOperator;
	}

	/**
	 * Resolves the column index of the given column property.
	 * @param colProp
	 * @return the column index
	 */
	private int resolveColumnIndex(String colProp) {
		for(int i = 0; i < columns.length; i++) {
			final Column c = columns[i];
			if(c.getPropertyName() != null && c.getPropertyName().equals(colProp)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Unresolveable column property: " + colProp);
	}

	private void applySorting(Sorting sorting) {
		assert sortlinks != null && sorting != null;
		final SortColumn sc = sorting.getPrimarySortColumn();

		// resolve the column index
		final int index = resolveColumnIndex(sc.getPropertyName());

		// reset old sort column (if there is one)
		if(crntSortColIndex >= 0) {
			sortlinks[crntSortColIndex].clearSortDirection();
		}

		// set new sort column
		sortlinks[index].setSortDirection(sc.getDirection());
		crntSortColIndex = index;
	}

	/**
	 * SortLink
	 * @author jpk
	 */
	private final class SortLink extends Composite implements ClickHandler {

		private final FlowPanel pnl = new FlowPanel();

		private final SimpleHyperLink lnk;

		private final Column column;

		private SortDir direction;

		/**
		 * Constructor
		 * @param column
		 */
		public SortLink(Column column) {
			assert column.getPropertyName() != null;
			lnk = new SimpleHyperLink(column.getName(), this);
			pnl.add(lnk);
			initWidget(pnl);
			this.column = column;
		}

		@SuppressWarnings("synthetic-access")
		public void setSortDirection(SortDir direction) {
			assert direction != null && pnl.getWidgetCount() == 1;

			this.direction = direction;

			final SortDir reverseDir = direction == SortDir.ASC ? SortDir.DESC : SortDir.ASC;
			final String reverseTitle = "Sort " + (reverseDir.getName());

			// set the title to the reverse of the current sort dir
			lnk.setTitle(reverseTitle);

			if(imgSortDir == null) {
				imgSortDir = new Image();
				imgSortDir.addClickHandler(this);
			}

			// insert the sort dir arrow image
			if(direction == SortDir.ASC) {
				imageBundle.sort_asc().applyTo(imgSortDir);
			}
			else {
				imageBundle.sort_desc().applyTo(imgSortDir);
			}
			imgSortDir.setStyleName(Styles.SORT);
			imgSortDir.setTitle(reverseTitle);
			pnl.insert(imgSortDir, 0);
		}

		public void clearSortDirection() {
			assert direction != null && pnl.getWidgetCount() == 2;
			direction = null;
			lnk.setTitle("Sort by " + column.getName());
			pnl.remove(0);
		}

		public void onClick(ClickEvent event) {
			if(event.getSource() == lnk) {
				final SortColumn sc =
					new SortColumn(column.getPropertyName(), column.getParentAlias(), direction == SortDir.ASC ? SortDir.DESC
							: SortDir.ASC,
							ignoreCaseWhenSorting);
				listingOperator.sort(new Sorting(sc));
			}
		}
	}

	private void addHeaderRow(IListingConfig<R> config) {
		final int numCols = columns.length;

		resize(1, numCols);
		getRowFormatter().addStyleName(0, Styles.HEAD);

		for(int c = 0; c < columns.length; c++) {
			final Column col = columns[c];
			final boolean isRowCntCol = Column.ROW_COUNT_COLUMN == col;
			if(isRowCntCol) {
				getCellFormatter().addStyleName(0, c, Styles.COUNT_COL);
				//getColumnFormatter().addStyleName(c, Styles.COUNT_COL);
			}
			if(config.isSortable()) {
				if(isRowCntCol) {
					setWidget(0, c, new Label("#"));
				}
				else if(col.getPropertyName() != null) {
					assert sortlinks != null;
					final SortLink sl = new SortLink(col);
					sortlinks[c] = sl;
					setWidget(0, c, sl);
				}
			}
			else {
				setWidget(0, c, new Label(col.getName()));
			}
		}
	}

	/**
	 * Sets row data.
	 * @param rowIndex
	 * @param rowNum The page related row number. If its value is -1, then it is
	 *        not set in the table.
	 * @param rowData The data by which the row's cells are populated
	 * @param overwriteOnNull Overwrite existing cell data when the corresponding
	 *        row data element is <code>null</code>?
	 */
	protected void setRowData(int rowIndex, int rowNum, R rowData, boolean overwriteOnNull) {
		if(rowIndex == 0) {
			return; // header row
		}

		for(int c = 0; c < columns.length; c++) {
			if(Column.ROW_COUNT_COLUMN == columns[c]) {
				getCellFormatter().addStyleName(rowIndex, c, Styles.COUNT_COL);
				if(rowNum > -1) {
					setText(rowIndex, c, Integer.toString(rowNum));
				}
			}
			else {
				String cv = cellRenderer.getCellValue(rowData, columns[c]);
				if(overwriteOnNull || cv != null) {
					if(cv == null) {
						cv = "-";
					}
					setText(rowIndex, c, cv);
				}
			}
		}
	}

	private void addBodyRows(R[] page, int offset) {
		final int numBodyRows = page.length;
		resizeRows(numBodyRows + 1);
		boolean evn = false;
		int rowIndex = offset;
		for(int r = 0; r < numBodyRows; r++) {
			getRowFormatter().addStyleName(r + 1, ((evn = !evn) ? Styles.EVEN : Styles.ODD));
			setRowData(r + 1, ++rowIndex, page[r], true);
		}
	}

	protected void removeBodyRows() {
		resizeRows(1);
	}

	public final void onListingEvent(ListingEvent<R> event) {
		if(event.getListingOp().isQuery()) {
			removeBodyRows();
			if(event.getPageElements() != null) {
				addBodyRows(event.getPageElements(), event.getOffset());
				final Sorting sorting = event.getSorting();
				if(sortlinks != null && sorting != null) applySorting(sorting);
				crntPage = event.getPageNum() + 1;
				numPages = event.getNumPages();
				actvRowIndex = crntRowIndex = -1; // reset
			}
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		switch(event.getTypeInt()) {

			case Event.ONMOUSEOVER:
				final Element td = getEventTargetCell(event);
				if(td == null) return;
				final Element tr = td.getParentElement();
				final Element tbody = tr.getParentElement();
				setActiveRow(DOM.getChildIndex((com.google.gwt.user.client.Element) tbody.cast(),
						(com.google.gwt.user.client.Element) tr.cast()));
				break;

			case Event.ONMOUSEOUT:
				if(actvRowIndex >= 0) {
					getRowFormatter().removeStyleName(actvRowIndex, Styles.ACTIVE);
					actvRowIndex = -1;
				}
				break;
		}
	}

	public void onClick(ClickEvent event) {
		if(event.getSource() == this) {
			final Cell cell = getCellForEvent(event);
			setCurrentRow(cell.getRowIndex());
		}
	}

	public void onKeyDown(KeyDownEvent event) {
		// if(sender != focusPanel) return;
		final int keyCode = event.getNativeKeyCode();
		if(keyCode == KeyCodes.KEY_UP) {
			setActiveRow(actvRowIndex - 1);
		}
		else if(keyCode == KeyCodes.KEY_DOWN) {
			setActiveRow(actvRowIndex + 1);
		}
		else if(keyCode == KeyCodes.KEY_ENTER) {
			setCurrentRow(actvRowIndex);
		}
		else if(keyCode == KeyCodes.KEY_PAGEUP) {
			if(crntPage > 1) {
				listingOperator.previousPage();
			}
		}
		else if(keyCode == KeyCodes.KEY_PAGEDOWN) {
			if(crntPage < numPages) {
				listingOperator.nextPage();
			}
		}
	}

	private void setActiveRow(int rowIndex) {
		if(rowIndex < 1 || rowIndex == actvRowIndex || rowIndex > getDOMRowCount() - 1) {
			return;
		}
		if(actvRowIndex >= 0) {
			getRowFormatter().removeStyleName(actvRowIndex, Styles.ACTIVE);
		}
		getRowFormatter().addStyleName(rowIndex, Styles.ACTIVE);
		actvRowIndex = rowIndex;
	}

	private void setCurrentRow(int rowIndex) {
		if(rowIndex < 1 || rowIndex == crntRowIndex || rowIndex > getDOMRowCount() - 1) {
			return;
		}
		if(crntRowIndex >= 0) {
			getRowFormatter().removeStyleName(crntRowIndex, Styles.CURRENT);
		}
		getRowFormatter().addStyleName(rowIndex, Styles.CURRENT);
		crntRowIndex = rowIndex;
		// DOM.scrollIntoView(targetTd);
	}

	/**
	 * Appends a new row to the table.
	 * @param rowData The row data for the new table row
	 * @return The index of the newly-created row
	 */
	int addRow(R rowData) {
		// insert a new empty row
		final int addRowIndex = getRowCount();
		resizeRows(addRowIndex + 1);

		// set the row data
		setRowData(addRowIndex, -1, rowData, true);

		getRowFormatter().addStyleName(addRowIndex, Styles.ADDED);

		return addRowIndex;
	}

	/**
	 * Updates an existing row's cell contents.
	 * @param rowIndex The row index of the row to update
	 * @param rowData The new row data to apply
	 */
	void updateRow(int rowIndex, R rowData) {
		assert rowIndex >= 1 : "Can't update the header row";
		setRowData(rowIndex, -1, rowData, true);
		getRowFormatter().addStyleName(rowIndex, Styles.UPDATED);
	}

	/**
	 * Removes a table row.
	 * @param rowIndex The row index of the row to remove
	 */
	void deleteRow(int rowIndex) {
		assert rowIndex >= 1 : "Can't delete the header row";

		removeRow(rowIndex);
		// update the numRows property
		numRows--;
		updateRowsBelow(rowIndex, false);

		// reset the current row index
		if(crntRowIndex == rowIndex) {
			crntRowIndex = -1;
		}
	}

	/**
	 * Marks a row as deleted but does not actually remove the table row.
	 * @param rowIndex The index of the row to mark deleted
	 * @param markDeleted Toggle on whether or not to mark or un-mark a row as
	 *        deleted
	 */
	void markRowDeleted(int rowIndex, boolean markDeleted) {
		assert rowIndex >= 1 : "Can't delete the header row";
		if(markDeleted)
			getRowFormatter().addStyleName(rowIndex, Styles.DELETED);
		else
			getRowFormatter().removeStyleName(rowIndex, Styles.DELETED);
	}

	public boolean isRowMarkedDeleted(int rowIndex) {
		final String sn = getRowFormatter().getStyleName(rowIndex);
		return sn == null ? false : sn.indexOf(Styles.DELETED) >= 0;
	}

	private int getPageRowNum(int rowIndex) {
		if(rowNumColIndex == -1) return -1;
		return Integer.parseInt(getText(rowIndex, rowNumColIndex));
	}

	/**
	 * Updates rows below a given row index subsequent to a row being either added
	 * or removed
	 * @param rowIndex The row index at which the successive rows below it are
	 *        updated
	 * @param add Due to a row being added (<code>true</code>) or removed (
	 *        <code>false</code>)?
	 */
	private void updateRowsBelow(int rowIndex, boolean add) {
		final int nrows = getDOMRowCount();
		int newPageRowNum = getPageRowNum(rowIndex) + (add ? +1 : -1);
		if(rowIndex > 0 && rowIndex <= nrows - 1) {
			for(int i = rowIndex; i < nrows; i++) {

				// update the row num col text (if showing)
				if(rowNumColIndex >= 0) {
					setText(i, rowNumColIndex, Integer.toString(newPageRowNum++));
				}

				// toggle the odd/even styling
				final HTMLTable.RowFormatter rf = getRowFormatter();
				if(rf.getStyleName(i).indexOf(Styles.EVEN) >= 0) {
					rf.removeStyleName(i, Styles.EVEN);
					rf.addStyleName(i, Styles.ODD);
				}
				else if(rf.getStyleName(i).indexOf(Styles.ODD) >= 0) {
					rf.removeStyleName(i, Styles.ODD);
					rf.addStyleName(i, Styles.EVEN);

				}
			}
		}
	}
}