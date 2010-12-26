/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.data.rpc;

import com.tll.IDescriptorProvider;
import com.tll.IMarshalable;
import com.tll.common.data.ListingOp;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.dao.Sorting;

/**
 * ListingRequest - Request data for performing server-side listing operations
 * against a particular listing.
 * @author jpk
 */
public final class ListingRequest implements IMarshalable, IDescriptorProvider {

	/**
	 * The unique listing name.
	 */
	private String listingId;

	/**
	 * The listing definition used to generate or refresh a listing.
	 */
	private RemoteListingDefinition<? extends IMarshalable> listingDef;

	private ListingOp listingOp;
	private Integer offset;
	private Sorting sorting;
	private boolean retainStateOnClear = true;

	/**
	 * Constructor
	 */
	public ListingRequest() {
		super();
	}

	/**
	 * Constructor - Used for fetching listing data against a non-cached server
	 * side listing.
	 * @param listingId The unique listing id
	 * @param listingDef The listing definition
	 * @param listingOp The listing op
	 * @param offset The listing index offset
	 * @param sorting The sorting directive
	 */
	public ListingRequest(String listingId, RemoteListingDefinition<? extends IMarshalable> listingDef,
			ListingOp listingOp, Integer offset,
			Sorting sorting) {
		super();
		this.listingId = listingId;
		this.listingDef = listingDef;
		this.listingOp = listingOp;
		this.offset = offset;
		this.sorting = sorting;
	}

	/**
	 * Constructor - Used for fetching listing data for a cached listing. If the
	 * listing is not cached server-side the response will indicate this and it is
	 * up to the client how to proceed.
	 * @param listingId The unique listing name
	 * @param offset The list index offset
	 * @param sorting The sorting directive
	 */
	public ListingRequest(String listingId, Integer offset, Sorting sorting) {
		super();
		this.listingId = listingId;
		this.listingOp = ListingOp.FETCH;
		this.offset = offset;
		this.sorting = sorting;
	}

	/**
	 * Constructor - Used for clearing an existing listing.
	 * @param listingId The unique listing name
	 * @param retainStateOnClear Retain the server side state when the listing is
	 *        cleared on the server?
	 */
	public ListingRequest(String listingId, boolean retainStateOnClear) {
		super();
		this.listingId = listingId;
		this.listingOp = ListingOp.CLEAR;
		this.retainStateOnClear = retainStateOnClear;
	}

	@Override
	public String descriptor() {
		return "'" + getListingId() + "' listing command";
	}

	/**
	 * Uniquely identifies a single listing.
	 * @return The unique listing name
	 */
	public String getListingId() {
		return listingId;
	}

	/**
	 * @return The listing definition used when generating or refreshing a
	 *         listing.
	 */
	public RemoteListingDefinition<? extends IMarshalable> getListingDef() {
		return listingDef;
	}

	/**
	 * Required by the server side table model operator.
	 * @return ListingOp
	 */
	public ListingOp getListingOp() {
		return listingOp;
	}

	/**
	 * @return The listing index offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @return The sorting directive.
	 */
	public Sorting getSorting() {
		return sorting;
	}

	/**
	 * Retain the listing state upon clear?
	 * @return true/false
	 */
	public boolean getRetainStateOnClear() {
		return retainStateOnClear;
	}
}
