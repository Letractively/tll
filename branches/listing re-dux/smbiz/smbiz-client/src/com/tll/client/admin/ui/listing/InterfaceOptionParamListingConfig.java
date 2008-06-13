/**
 * The Logic Lab
 * @author jpk
 * Apr 21, 2008
 */
package com.tll.client.admin.ui.listing;

import com.tll.client.listing.Column;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.model.Model;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * InterfaceOptionParamListingConfig
 * @author jpk
 */
public final class InterfaceOptionParamListingConfig implements IListingConfig<Model> {

	public String getListingName() {
		return EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION.name() + "_LISTING";
	}

	public String getListingElementName() {
		return "Parameter";
	}

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

	public final String getCaption() {
		return null;
	}

	public final ITableCellRenderer<Model> getCellRenderer() {
		return IListingConfig.MODEL_DATA_CELL_RENDERER;
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
		return false;
	}

	public final boolean isSortable() {
		return false;
	}
}
