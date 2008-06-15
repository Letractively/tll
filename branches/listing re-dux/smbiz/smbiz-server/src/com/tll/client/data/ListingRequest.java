/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.data;

import com.tll.client.search.ISearch;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;

/**
 * ListingRequest
 * @author jpk
 */
public final class ListingRequest<S extends ISearch> {

	private String listingName;
	private ListHandlerType listHandlerType;
	private String[] propKeys;
	private int pageSize;
	private Boolean retainStateOnClear = Boolean.TRUE;

	private ListingOp listingOp;
	private S searchCriteria;
	private Sorting sorting;
	private Integer offset;

	/**
	 * Constructor
	 */
	public ListingRequest() {
		super();
	}

	/**
	 * Constructor - Used for generating a fresh listing.
	 * @param listingName
	 * @param listHandlerType
	 * @param propKeys
	 * @param pageSize
	 * @param searchCriteria
	 * @param listingOp
	 */
	public ListingRequest(String listingName, ListHandlerType listHandlerType, String[] propKeys, int pageSize,
			S searchCriteria, ListingOp listingOp) {
		super();
		this.listingName = listingName;
		this.listHandlerType = listHandlerType;
		this.propKeys = propKeys;
		this.pageSize = pageSize;
		this.searchCriteria = searchCriteria;
		this.listingOp = listingOp;
	}

	/**
	 * Constructor - Used for altering an existing listing.
	 * @param listingName
	 * @param listingOp
	 */
	public ListingRequest(String listingName, ListingOp listingOp) {
		super();
		this.listingName = listingName;
		this.listingOp = listingOp;
	}

	public String descriptor() {
		return "'" + getListingName() + "' listing command";
	}

	/**
	 * Paging switch. If <code>false</code>, no paging is applied.
	 * @return true/false
	 */
	public boolean isPageable() {
		return pageSize > -1;
	}

	/**
	 * The max number of elements to show per page.
	 * @return int
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return The list handling type. Controls how paging is performed on the
	 *         server.
	 */
	public ListHandlerType getListHandlerType() {
		return listHandlerType;
	}

	/**
	 * @return the distinguishing name of the listing. Must be unique against all
	 *         other loaded listings as it is used for caching.
	 */
	public String getListingName() {
		return listingName;
	}

	public void setListingName(String listingName) {
		this.listingName = listingName;
	}

	/**
	 * @return the prop keys array.
	 */
	public String[] getPropKeys() {
		return propKeys;
	}

	public void setPropKeys(String[] propKeys) {
		this.propKeys = propKeys;
	}

	/**
	 * Required by the server side table model operator.
	 * @return ListingOp
	 */
	public ListingOp getListingOp() {
		return listingOp;
	}

	/**
	 * @return The search criteria for the listing.
	 */
	public S getSearchCriteria() {
		return searchCriteria;
	}

	/**
	 * @return The listing index offset
	 */
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer pageNumber) {
		this.offset = pageNumber;
	}

	/**
	 * @return The sorting directive.
	 */
	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	/**
	 * Retain the listing state upon clear?
	 * @return true/false
	 */
	public Boolean getRetainStateOnClear() {
		return retainStateOnClear;
	}

	public void setRetainStateOnClear(Boolean retainStateOnClear) {
		this.retainStateOnClear = retainStateOnClear;
	}

}
