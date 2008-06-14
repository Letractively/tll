/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.IData;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * DataListingWidget - Listing that lists artitrary data.
 * @author jpk
 */
public final class DataListingWidget<R extends IData> extends ListingWidget<R> {

	private final ListingTable<R> table;

	/**
	 * Constructor
	 * @param config
	 * @param addRowDelegate
	 */
	public DataListingWidget(IListingConfig<R> config, IAddRowDelegate addRowDelegate) {
		super(config, addRowDelegate);
		// create and initialize the table panel
		table = new ListingTable<R>(config);
		portal.add(table);
		focusPanel.addKeyboardListener(table);
	}

	public void addTableListener(TableListener listener) {
		table.addTableListener(listener);
	}

	public void removeTableListener(TableListener listener) {
		table.removeTableListener(listener);
	}

	/**
	 * Sets the listing operator for this listing.
	 * @param operator the operator to set
	 */
	@Override
	public void setOperator(IListingOperator operator) {
		super.setOperator(operator);
		table.setListingOperator(operator);
	}

	@Override
	public final void addRow(R rowData) {
		super.addRow(rowData);
		table.addRow(rowData);
	}

	@Override
	public void updateRow(int rowIndex, R rowData) {
		super.updateRow(rowIndex, rowData);
		table.updateRow(rowIndex, rowData);
	}

	@Override
	public void deleteRow(int rowIndex) {
		super.deleteRow(rowIndex);
		table.deleteRow(rowIndex);
	}

	@Override
	public void markRowDeleted(int rowIndex) {
		super.markRowDeleted(rowIndex);
		table.markRowDeleted(rowIndex);
	}

	@Override
	public void setPage(IPage<R> page, Sorting sorting) {
		super.setPage(page, sorting);
		table.setPage(page, sorting);
	}
}
