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
public final class ListingCommand implements IListingCommand {

	private ListHandlerType listHandlerType;
	private String listingName;
	private int pageSize;
	private PropKey[] propKeys;
	private ISearch searchCriteria;

	private ListingOp listingOp;
	private Integer pageNumber;
	private Sorting sorting;
	private Boolean retainStateOnClear = Boolean.TRUE;

	/**
	 * Constructor
	 */
	public ListingCommand() {
		super();
	}

	/**
	 * Constructor - Used for generating a fresh listing.
	 * @param pageSize
	 * @param listHandlerType
	 * @param listingName
	 * @param propKeys
	 * @param listingOp
	 * @param searchCriteria
	 */
	public ListingCommand(int pageSize, ListHandlerType listHandlerType, String listingName, PropKey[] propKeys,
			ListingOp listingOp, ISearch searchCriteria) {
		super();
		this.pageSize = pageSize;
		this.listHandlerType = listHandlerType;
		this.listingName = listingName;
		this.propKeys = propKeys;
		this.listingOp = listingOp;
		this.searchCriteria = searchCriteria;
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

	public PropKey[] getPropKeys() {
		return propKeys;
	}

	public void setPropKeys(PropKey[] propKeys) {
		this.propKeys = propKeys;
	}

	public ListingOp getListingOp() {
		return listingOp;
	}

	public void setOpContext(ListingOp opContext) {
		this.listingOp = opContext;
	}

	public ISearch getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(ISearch searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
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
