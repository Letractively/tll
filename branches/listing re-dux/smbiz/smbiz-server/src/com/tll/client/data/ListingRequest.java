/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.data;

import com.tll.client.IMarshalable;
import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;
import com.tll.util.IDescriptorProvider;

/**
 * ListingRequest - Request data for performing server-side listing operations
 * against a particular listing.
 * @author jpk
 */
public final class ListingRequest<E extends IEntity> implements IMarshalable, IDescriptorProvider {

	/**
	 * The unique listing name.
	 */
	private String listingName;

	/**
	 * The listing definition used to generate or refresh a listing.
	 */
	private RemoteListingDefinition<E> listingDef;

	private ListingOp listingOp;
	private Integer offset;
	private Sorting sorting;
	private Boolean retainStateOnClear = Boolean.TRUE;

	/**
	 * Constructor
	 */
	public ListingRequest() {
		super();
	}

	/**
	 * Constructor - Used for fetching listing data against a non-cached server
	 * side listing.
	 * @param listingName The unique listing name
	 * @param listingDef The listing definition
	 * @param listingOp The listing op
	 * @param offset The listing index offset
	 * @param sorting The sorting directive
	 */
	public ListingRequest(String listingName, RemoteListingDefinition<E> listingDef, ListingOp listingOp, Integer offset,
			Sorting sorting) {
		super();
		this.listingName = listingName;
		this.listingDef = listingDef;
		this.listingOp = listingOp;
		this.offset = offset;
		this.sorting = sorting;
	}

	/**
	 * Constructor - Used for fetching listing data for a cached listing. If the
	 * listing is not cached server-side the response will indicate this and it is
	 * up to the client how to proceed.
	 * @param listingName The unique listing name
	 * @param offset The list index offset
	 * @param sorting The sorting directive
	 */
	public ListingRequest(String listingName, Integer offset, Sorting sorting) {
		super();
		this.listingName = listingName;
		this.listingOp = ListingOp.FETCH;
		this.offset = offset;
		this.sorting = sorting;
	}

	/**
	 * Constructor - Used for clearing an existing listing.
	 * @param listingName The unique listing name
	 * @param retainStateOnClear Retain the server side state when the listing is
	 *        cleared on the server?
	 */
	public ListingRequest(String listingName, Boolean retainStateOnClear) {
		super();
		this.listingName = listingName;
		this.listingOp = ListingOp.CLEAR;
		this.retainStateOnClear = retainStateOnClear;
	}

	public String descriptor() {
		return "'" + getListingName() + "' listing command";
	}

	/**
	 * Uniquely identifies a single listing.
	 * @return The unique listing name
	 */
	public String getListingName() {
		return listingName;
	}

	/**
	 * @return The listing definition used when generating or refreshing a
	 *         listing.
	 */
	public RemoteListingDefinition<E> getListingDef() {
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
	public Boolean getRetainStateOnClear() {
		return retainStateOnClear;
	}
}
