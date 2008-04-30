/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.admin.ui.listing;

import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.ITableCellTransformer;
import com.tll.client.model.Model;
import com.tll.listhandler.Sorting;

/**
 * AccountListingConfig
 * @author jpk
 */
public abstract class AccountListingConfig implements IListingConfig {

	private final Sorting defaultSorting = new Sorting(Model.NAME_PROPERTY);

	public final Sorting getDefaultSorting() {
		return defaultSorting;
	}

	public final String getCaption() {
		return null;
	}

	public final ITableCellTransformer getTableCellTransformer() {
		return IListingConfig.DEFAULT_TABLE_CELL_TRANSFORMER;
	}

	public final boolean isPageable() {
		return true;
	}

	public int getPageSize() {
		return IListingConfig.DEFAULT_PAGE_SIZE;
	}

	public final boolean isShowAddBtn() {
		return true;
	}

	public final boolean isShowNavBar() {
		return true;
	}

	public final boolean isShowRefreshBtn() {
		return true;
	}

	public final boolean isSortable() {
		return true;
	}
}
