/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.listing;

import com.tll.client.listing.AbstractListingConfig;
import com.tll.client.listing.Column;
import com.tll.common.model.Model;
import com.tll.dao.Sorting;

/**
 * AccountListingConfig
 * @author jpk
 */
public abstract class AccountListingConfig extends AbstractListingConfig<Model> {

	/**
	 * Constructor
	 * @param caption
	 * @param listingElementName
	 * @param modelProps
	 * @param cols
	 * @param defaultSorting
	 * @param pageSize
	 */
	public AccountListingConfig(String caption, String listingElementName, String[] modelProps, Column[] cols,
			Sorting defaultSorting, int pageSize) {
		super(caption, listingElementName, modelProps, cols, defaultSorting, pageSize);
	}

	/**
	 * Constructor
	 * @param caption
	 * @param listingElementName
	 * @param modelProps
	 * @param cols
	 * @param defaultSorting
	 */
	public AccountListingConfig(String caption, String listingElementName, String[] modelProps, Column[] cols,
			Sorting defaultSorting) {
		super(caption, listingElementName, modelProps, cols, defaultSorting);
	}

	@Override
	public final boolean isShowNavBar() {
		return true;
	}

	@Override
	public final boolean isShowRefreshBtn() {
		return false;
	}

	@Override
	public final boolean isSortable() {
		return true;
	}
}
