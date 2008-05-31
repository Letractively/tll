/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.ListingOp;
import com.tll.client.model.Model;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * ListingEvent - Fired when listing data is retrieved for a targeted listing.
 * @author jpk
 */
public final class ListingEvent extends BaseEvent {

	private final boolean success;
	private final String listingName;
	private final ListingOp listingOp;
	private final IPage<Model> page;
	private final Sorting sorting;

	/**
	 * Constructor - Use when the listing op changes the page data
	 * @param source
	 * @param listingName
	 * @param success
	 * @param listingOp
	 * @param page
	 * @param sorting
	 */
	public ListingEvent(Widget source, String listingName, boolean success, ListingOp listingOp, IPage<Model> page,
			Sorting sorting) {
		super(source);
		this.success = success;
		this.listingName = listingName;
		this.listingOp = listingOp;
		this.page = page;
		this.sorting = sorting;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getListingName() {
		return listingName;
	}

	public ListingOp getListingOp() {
		return listingOp;
	}

	public IPage<Model> getPage() {
		return page;
	}

	public Sorting getSorting() {
		return sorting;
	}
}
