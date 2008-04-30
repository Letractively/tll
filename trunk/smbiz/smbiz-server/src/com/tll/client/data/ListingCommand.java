/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.data;

import com.tll.client.search.ISearch;

/**
 * ListingCommand - {@link IListingCommand} impl.
 * @author jpk
 */
public final class ListingCommand implements IListingCommand {

	private int pageSize;
	private int listHandlerType;
	private String listingName;
	private PropKey[] propKeys;
	private ListingOp listingOp;
	private ISearch criteria;

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
	 * @param opContext
	 * @param criteria
	 */
	public ListingCommand(int pageSize, int listHandlerType, String listingName, PropKey[] propKeys, ListingOp opContext,
			ISearch criteria) {
		super();
		this.pageSize = pageSize;
		this.listHandlerType = listHandlerType;
		this.listingName = listingName;
		this.propKeys = propKeys;
		this.listingOp = opContext;
		this.criteria = criteria;
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

	public int getListHandlerType() {
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
		return criteria;
	}

	public void setSearchCriteria(ISearch criteria) {
		this.criteria = criteria;
	}
}
