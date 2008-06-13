/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.ui.listing;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.data.ListingOp;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.model.IData;
import com.tll.client.model.RefKey;
import com.tll.client.ui.CSS;
import com.tll.client.ui.SimpleHyperLink;
import com.tll.listhandler.IPage;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.SortDir;
import com.tll.listhandler.Sorting;

/**
 * ListingTable - ListingWidget specific HTML table.
 * @author jpk
 */
public class ListingTable<R extends IData> extends Grid implements TableListener, KeyboardListener {

	/**
	 * The actual HTML table tag containing the listing data gets this style (CSS
	 * class).
	 */
	protected static final String STYLE_TABLE = "table";
	protected static final String CSS_SORT = "sort";
	protected static final String CSS_COUNT_COL = "countCol";
	protected static final String CSS_HEAD = "head";
	protected static final String CSS_EVEN = "even";
	protected static final String CSS_ODD = "odd";

	protected static final String CSS_ADDED = "added";
	protected static final String CSS_UPDATED = "updated";
	protected static final String CSS_DELETED = "deleted";

	/**
	 * The sort column arrow.
	 */
	private Image imgSortDir;

	protected Column[] columns;

	protected ITableCellRenderer<R> cellTransformer;

	protected IListingOperator listingOperator;

	/**
	 * The column index holding the row num. -1 indicates the row num col doesn't
	 * exist.
	 */
	protected int rowNumColIndex;

	/**
	 * {@link RefKey}s for each listing element row.
	 */
	protected final List<RefKey> rowRefs = new ArrayList<RefKey>();

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

	private int crntPage = -1;

	private int numPages = 0;

	/**
	 * Constructor
	 * @para config
	 */
	public ListingTable(IListingConfig<R> config) {
		super();
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		addTableListener(this);
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
		this.cellTransformer = config.getCellRenderer();
		assert columns != null && cellTransformer != null;

		int rn = -1;
		Column[] columns = config.getColumns();
		for(int i = 0; i < columns.length; i++) {
			if(Column.ROW_COUNT_COL_PROP.equals(columns[i].getPropertyName())) {
				rn = i;
				break;
			}
		}
		rowNumColIndex = rn;

		if(config.isSortable()) {
			sortlinks = new ListingTable.SortLink[columns.length];
		}

		setStyleName(STYLE_TABLE);

		addHeaderRow(config);
	}

	/**
	 * @param listingOperator the listingOperator to set
	 */
	public final void setListingOperator(IListingOperator listingOperator) {
		this.listingOperator = listingOperator;
	}

	public final Widget getTableWidget() {
		return this;
	}

	/**
	 * Get the row ref for a given row.
	 * @param row 0-based table row num (considers the header row).
	 * @return RefKey
	 */
	public final RefKey getRowRef(int row) {
		return rowRefs.get(row - 1);
	}

	/**
	 * Get the row index given a {@link RefKey}.
	 * @param rowRef The RefKey for which to find the associated row index.
	 * @return The row index or <code>-1</code> if no row matching the given ref
	 *         key is present in the table.
	 */
	final int getRowIndex(RefKey rowRef) {
		return rowRefs.indexOf(rowRef) + 1; // account for header row
	}

	/**
	 * Resolves the column index of the given column property.
	 * @param colProp
	 * @return the column index
	 */
	private int resolveColumnIndex(String colProp) {
		for(int i = 0; i < columns.length; i++) {
			Column c = columns[i];
			if(c.getPropertyName().equals(colProp)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Unresolveable column property: " + colProp);
	}

	private void applySorting(Sorting sorting) {
		assert sortlinks != null && sorting != null;
		SortColumn sc = sorting.getPrimarySortColumn();

		// resolve the column index
		int index = resolveColumnIndex(sc.getPropertyName());

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
	private final class SortLink extends Composite implements ClickListener {

		private final FlowPanel pnl = new FlowPanel();

		private final SimpleHyperLink lnk;

		private final Column column;

		private SortDir direction;

		/**
		 * Constructor
		 * @param column
		 */
		public SortLink(Column column) {
			lnk = new SimpleHyperLink(column.name, this);
			pnl.add(lnk);
			initWidget(pnl);
			this.column = column;
		}

		public void setSortDirection(SortDir direction) {
			assert direction != null && pnl.getWidgetCount() == 1;

			this.direction = direction;

			SortDir reverseDir = direction == SortDir.ASC ? SortDir.DESC : SortDir.ASC;
			String reverseTitle = "Sort " + (reverseDir.getName());

			// set the title to the reverse of the current sort dir
			lnk.setTitle(reverseTitle);

			if(imgSortDir == null) {
				imgSortDir = new Image();
				imgSortDir.addClickListener(this);
				imgSortDir.setStyleName(CSS_SORT);
			}

			// insert the sort dir arrow image
			if(direction == SortDir.ASC) {
				App.imgs().sort_asc().applyTo(imgSortDir);
			}
			else {
				App.imgs().sort_desc().applyTo(imgSortDir);
			}
			imgSortDir.setTitle(reverseTitle);
			pnl.insert(imgSortDir, 0);
		}

		public void clearSortDirection() {
			assert direction != null && pnl.getWidgetCount() == 2;
			direction = null;
			lnk.setTitle("Sort by " + column.name);
			pnl.remove(0);
		}

		public void onClick(Widget sender) {
			if(sender == lnk) {
				SortColumn sc = new SortColumn(column.getPropertyName(), column.getParentAlias(), column.getIgnoreCase());
				sc.setDirection(direction == SortDir.ASC ? SortDir.DESC : SortDir.ASC);
				listingOperator.sort(sc);
			}
		}
	}

	private void addHeaderRow(IListingConfig<R> config) {
		final int numCols = columns.length;

		resize(1, numCols);
		getRowFormatter().addStyleName(0, CSS_HEAD);

		for(int c = 0; c < columns.length; c++) {
			Column col = columns[c];
			boolean isRowCntCol = Column.ROW_COUNT_COL_PROP.equals(col.getPropertyName());
			if(isRowCntCol) {
				getCellFormatter().addStyleName(0, c, CSS_COUNT_COL);
				getColumnFormatter().addStyleName(c, CSS_COUNT_COL);
			}
			if(config.isSortable()) {
				if(isRowCntCol) {
					setWidget(0, c, new Label("#"));
				}
				else {
					assert sortlinks != null;
					SortLink sl = new SortLink(col);
					sortlinks[c] = sl;
					setWidget(0, c, sl);
				}
			}
			else {
				setWidget(0, c, new Label(col.name));
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
	private void setRowData(int rowIndex, int rowNum, R rowData, boolean overwriteOnNull) {
		if(rowIndex == 0) {
			return; // header row
		}

		final String[] cellVals = cellTransformer.getCellValues(rowData, columns);
		for(int c = 0; c < columns.length; c++) {
			if(Column.ROW_COUNT_COL_PROP.equals(columns[c].getPropertyName())) {
				if(rowNum > -1) {
					getCellFormatter().addStyleName(rowIndex, c, CSS_COUNT_COL);
					setText(rowIndex, c, Integer.toString(rowNum));
				}
			}
			else {
				String cv = cellVals[c];
				if(overwriteOnNull || cv != null) {
					if(cv == null) {
						cv = "-";
					}
					setText(rowIndex, c, cv);
				}
			}
		}
	}

	private void addBodyRows(IPage<R> page) {
		final int numBodyRows = page.getNumPageElements();
		resizeRows(numBodyRows + 1);
		rowRefs.clear();
		boolean evn = false;
		int rowNum = page.getFirstIndex();
		for(int r = 0; r < numBodyRows; r++) {
			getRowFormatter().addStyleName(r + 1, ((evn = !evn) ? CSS_EVEN : CSS_ODD));
			R data = page.getPageElements().get(r);
			setRowData(r + 1, ++rowNum, page.getPageElements().get(r), true);
			rowRefs.add(data.getRefKey());
		}
	}

	protected void removeBodyRows() {
		resizeRows(1);
	}

	public void setPage(IPage<R> page, Sorting sorting) {
		removeBodyRows();
		addBodyRows(page);
		if(sortlinks != null && sorting != null) applySorting(sorting);
		crntPage = page.getPageNumber() + 1;
		numPages = page.getNumPages();
		actvRowIndex = crntRowIndex = -1; // reset
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		switch(event.getTypeInt()) {

			case Event.ONMOUSEOVER:
				Element td = getEventTargetCell(event);
				if(td == null) return;
				Element tr = td.getParentElement();
				Element tbody = tr.getParentElement();
				setActiveRow(DOM.getChildIndex((com.google.gwt.user.client.Element) tbody.cast(),
						(com.google.gwt.user.client.Element) tr.cast()));
				break;

			case Event.ONMOUSEOUT:
				if(actvRowIndex >= 0) {
					getRowFormatter().removeStyleName(actvRowIndex, CSS.ACTIVE);
					actvRowIndex = -1;
				}
				break;
		}
	}

	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
		if(sender == this) {
			setCurrentRow(row);
		}
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		// if(sender != focusPanel) return;
		if(keyCode == KeyboardListener.KEY_UP) {
			setActiveRow(actvRowIndex - 1);
		}
		else if(keyCode == KeyboardListener.KEY_DOWN) {
			setActiveRow(actvRowIndex + 1);
		}
		else if(keyCode == KeyboardListener.KEY_ENTER) {
			setCurrentRow(actvRowIndex);
		}
		else if(keyCode == KeyboardListener.KEY_PAGEUP) {
			if(crntPage > 1) {
				listingOperator.navigate(ListingOp.PREVIOUS_PAGE, null);
			}
		}
		else if(keyCode == KeyboardListener.KEY_PAGEDOWN) {
			if(crntPage < numPages) {
				listingOperator.navigate(ListingOp.NEXT_PAGE, null);
			}
		}
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
	}

	private void setActiveRow(int rowIndex) {
		if(rowIndex < 1 || rowIndex == actvRowIndex || rowIndex > getDOMRowCount() - 1) {
			return;
		}
		if(actvRowIndex >= 0) {
			getRowFormatter().removeStyleName(actvRowIndex, CSS.ACTIVE);
		}
		getRowFormatter().addStyleName(rowIndex, CSS.ACTIVE);
		actvRowIndex = rowIndex;
	}

	private void setCurrentRow(int rowIndex) {
		if(rowIndex < 1 || rowIndex == crntRowIndex || rowIndex > getDOMRowCount() - 1) {
			return;
		}
		if(crntRowIndex >= 0) {
			getRowFormatter().removeStyleName(crntRowIndex, CSS.CURRENT);
		}
		getRowFormatter().addStyleName(rowIndex, CSS.CURRENT);
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
		rowRefs.add(rowData.getRefKey());

		getRowFormatter().addStyleName(addRowIndex, CSS_ADDED);

		return addRowIndex;
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
		rowRefs.remove(rowIndex - 1);
		updateRowsBelow(rowIndex, false);

		// reset the current row index
		if(crntRowIndex == rowIndex) {
			crntRowIndex = -1;
		}
	}

	/**
	 * Marks a row as deleted but does not actually remove the table row.
	 * @param rowIndex The index of the row to mark deleted
	 */
	void markRowDeleted(int rowIndex) {
		assert rowIndex >= 1 : "Can't delete the header row";
		getRowFormatter().addStyleName(rowIndex, CSS_DELETED);
	}

	/**
	 * Updates an existing row's cell contents.
	 * @param rowIndex The row index of the row to update
	 * @param rowData The new row data to apply
	 */
	void updateRow(int rowIndex, R rowData) {
		assert rowIndex >= 1 : "Can't update the header row";
		setRowData(rowIndex, -1, rowData, true);
		rowRefs.set(rowIndex - 1, rowData.getRefKey());
		getRowFormatter().addStyleName(rowIndex, CSS_UPDATED);
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
	 * @param add Due to a row being added (<code>true</code>) or removed (<code>false</code>)?
	 */
	private void updateRowsBelow(int rowIndex, boolean add) {
		final int numRows = getDOMRowCount();
		int newPageRowNum = getPageRowNum(rowIndex) + (add ? +1 : -1);
		if(rowIndex > 0 && rowIndex <= numRows - 1) {
			for(int i = rowIndex; i < numRows; i++) {

				// update the row num col text (if showing)
				if(rowNumColIndex >= 0) {
					setText(i, rowNumColIndex, Integer.toString(newPageRowNum++));
				}

				// toggle the odd/even styling
				HTMLTable.RowFormatter rf = getRowFormatter();
				if(rf.getStyleName(i).indexOf("even") >= 0) {
					rf.removeStyleName(i, "even");
					rf.addStyleName(i, "odd");
				}
				else if(rf.getStyleName(i).indexOf("odd") >= 0) {
					rf.removeStyleName(i, "odd");
					rf.addStyleName(i, "even");

				}
			}
		}
	}
}