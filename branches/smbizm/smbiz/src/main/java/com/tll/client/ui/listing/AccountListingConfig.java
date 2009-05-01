/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.ui.listing;

import com.tll.client.listing.IListingConfig;
import com.tll.common.model.Model;

/**
 * AccountListingConfig
 * @author jpk
 */
public abstract class AccountListingConfig implements IListingConfig<Model> {

	public boolean isIgnoreCaseWhenSorting() {
		return true;
	}

	public final String getCaption() {
		return null;
	}

	public int getPageSize() {
		return IListingConfig.DEFAULT_PAGE_SIZE;
	}

	public final boolean isShowNavBar() {
		return true;
	}

	public final boolean isShowRefreshBtn() {
		return false;
	}

	public final boolean isSortable() {
		return true;
	}
}
