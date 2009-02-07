/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.admin.ui.listing;

import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.ITableCellRenderer;
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

	public final ITableCellRenderer<Model> getCellRenderer() {
		return IListingConfig.MODEL_CELL_RENDERER;
	}

	public int getPageSize() {
		return IListingConfig.DEFAULT_PAGE_SIZE;
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
