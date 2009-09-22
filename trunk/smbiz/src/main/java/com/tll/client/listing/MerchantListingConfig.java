/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.client.listing;

import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;
import com.tll.dao.Sorting;


/**
 * MerchantListingConfig
 * @author jpk
 */
public class MerchantListingConfig extends AccountListingConfig {

	private static final String listingElementName = SmbizEntityType.MERCHANT.getName();

	private static final Sorting defaultSorting = new Sorting("name");

	private static final Column[] cols = new Column[] {
		Column.ROW_COUNT_COLUMN,
		new Column("Name", Model.NAME_PROPERTY, "rowData"),
		new Column("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "rowData"),
		new Column("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "rowData"),
		new Column("Status", "status", "rowData"),
		new Column("Billing Model", "billingModel", "rowData"),
		new Column("Billing Cycle", "billingCycle", "rowData"),
		new Column("Store Name", "storeName", "rowData")
	};

	private static final String[] mprops = new String[] {
		Model.NAME_PROPERTY,
		Model.DATE_CREATED_PROPERTY,
		Model.DATE_MODIFIED_PROPERTY,
		"status",
		"billingModel",
		"billingCycle",
		"storeName",
	};

	/**
	 * Constructor
	 */
	public MerchantListingConfig() {
		super(null, listingElementName, mprops, cols, defaultSorting);
	}
}
