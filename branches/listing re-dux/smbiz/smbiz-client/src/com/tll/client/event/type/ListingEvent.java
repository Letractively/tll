/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.ListingOp;
import com.tll.client.data.ListingPayload;
import com.tll.client.data.ListingPayload.ListingStatus;
import com.tll.client.model.IData;
import com.tll.listhandler.Sorting;

/**
 * ListingEvent - Fired when listing data is retrieved for a targeted listing.
 * @param <R> The row data type
 * @author jpk
 */
public final class ListingEvent<R extends IData> extends BaseEvent {

	private final ListingOp listingOp;
	private final ListingPayload<R> listingPayload;

	/**
	 * Constructor
	 * @param source
	 * @param listingOp
	 * @param listingPayload
	 */
	public ListingEvent(Widget source, ListingOp listingOp, ListingPayload<R> listingPayload) {
		super(source);
		if(listingOp == null || listingPayload == null) throw new IllegalArgumentException();
		this.listingOp = listingOp;
		this.listingPayload = listingPayload;
	}

	public boolean isSuccess() {
		return !listingPayload.hasErrors();
	}

	public String getListingName() {
		return listingPayload.getListingName();
	}

	public ListingOp getListingOp() {
		return listingOp;
	}

	public ListingStatus getListingStatus() {
		return listingPayload.getListingStatus();
	}

	public List<R> getPageElements() {
		return listingPayload.getPageElements();
	}

	public Integer getListSize() {
		return listingPayload.getListSize();
	}

	public Integer getOffset() {
		return listingPayload.getOffset();
	}

	public Sorting getSorting() {
		return listingPayload.getSorting();
	}

	@Override
	public String toString() {
		return getListingName();
	}
}
