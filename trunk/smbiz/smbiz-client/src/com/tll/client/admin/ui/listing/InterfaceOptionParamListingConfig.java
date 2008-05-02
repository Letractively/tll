/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.admin.ui.listing;

import com.tll.client.data.PropKey;
import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.ITableCellTransformer;
import com.tll.listhandler.Sorting;

/**
 * AccountListingConfig
 * @author jpk
 */
public final class InterfaceOptionParamListingConfig implements IListingConfig {

	private final PropKey[] propKeys = {
		new PropKey("name"),
		new PropKey("code"),
		new PropKey("description") };

	private final Column[] columns = {
		new Column("Name", "name", "intf"),
		new Column("Code", "code", "intf"),
		new Column("Desc", "description", "intf") };

	public final Sorting getDefaultSorting() {
		return null;
	}

	public Column[] getColumns() {
		return columns;
	}

	public String getListingElementName() {
		return "Parameter";
	}

	public PropKey[] getPropKeys() {
		return propKeys;
	}

	public final String getCaption() {
		return null;
	}

	public final ITableCellTransformer getTableCellTransformer() {
		return IListingConfig.DEFAULT_TABLE_CELL_TRANSFORMER;
	}

	public final boolean isPageable() {
		return false;
	}

	public int getPageSize() {
		return -1;
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
