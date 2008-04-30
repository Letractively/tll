/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.ClientListingOp;
import com.tll.client.model.Model;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * ListingEvent - Fired when a listing related event occurrs.
 * @author jpk
 */
public final class ListingEvent extends BaseEvent {

	private final boolean success;
	private final ClientListingOp listingOp;
	private final IPage<Model> page;
	private final int rowIndex;
	/**
	 * Used for updating a particular row's data
	 */
	private final Model rowData;

	private final Sorting sorting;

	/**
	 * Constructor - Use when the listing op changes the page data
	 * @param source
	 * @param success
	 * @param listingOp
	 * @param page
	 * @param sorting
	 */
	public ListingEvent(Widget source, boolean success, ClientListingOp listingOp, IPage<Model> page, Sorting sorting) {
		super(source);
		this.success = success;
		this.listingOp = listingOp;
		this.page = page;
		this.rowIndex = -1;
		this.rowData = null;
		this.sorting = sorting;
	}

	/**
	 * Constructor - Use when the listing op targets a particular row.
	 * @param source
	 * @param success
	 * @param listingOp
	 * @param rowIndex
	 * @param rowData
	 */
	public ListingEvent(Widget source, boolean success, ClientListingOp listingOp, int rowIndex, Model rowData) {
		super(source);
		this.success = success;
		this.listingOp = listingOp;
		this.page = null;
		this.rowIndex = rowIndex;
		this.rowData = rowData;
		this.sorting = null;
	}

	public boolean isSuccess() {
		return success;
	}

	public ClientListingOp getListingOp() {
		return listingOp;
	}

	public IPage<Model> getPage() {
		return page;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public Model getRowData() {
		return rowData;
	}

	public Sorting getSorting() {
		return sorting;
	}
}
