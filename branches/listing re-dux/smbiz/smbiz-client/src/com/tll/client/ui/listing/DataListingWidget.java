/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.TableListener;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.model.IData;

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
	public void onListingEvent(ListingEvent<R> event) {
		super.onListingEvent(event);
		table.onListingEvent(event);
	}

}
