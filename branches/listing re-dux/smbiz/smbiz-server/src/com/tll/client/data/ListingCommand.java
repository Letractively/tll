/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.data;

import com.tll.client.search.ISearch;
import com.tll.listhandler.ListHandlerType;
import com.tll.listhandler.Sorting;

/**
 * ListingCommand - {@link IListingCommand} impl.
 * @author jpk
 */
public final class ListingCommand<S extends ISearch> implements IListingCommand<S> {

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
	public ListingCommand() {
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
	public ListingCommand(String listingName, ListHandlerType listHandlerType, String[] propKeys, int pageSize,
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
	public ListingCommand(String listingName, ListingOp listingOp) {
		super();
		this.listingName = listingName;
		this.listingOp = listingOp;
	}

	public String descriptor() {
		return "'" + getListingName() + "' listing command";
	}

	public boolean isPageable() {
		return pageSize > -1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public ListHandlerType getListHandlerType() {
		return listHandlerType;
	}

	public String getListingName() {
		return listingName;
	}

	public void setListingName(String listingName) {
		this.listingName = listingName;
	}

	public String[] getPropKeys() {
		return propKeys;
	}

	public void setPropKeys(String[] propKeys) {
		this.propKeys = propKeys;
	}

	public ListingOp getListingOp() {
		return listingOp;
	}

	public S getSearchCriteria() {
		return searchCriteria;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer pageNumber) {
		this.offset = pageNumber;
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	public Boolean getRetainStateOnClear() {
		return retainStateOnClear;
	}

	public void setRetainStateOnClear(Boolean retainStateOnClear) {
		this.retainStateOnClear = retainStateOnClear;
	}

}
