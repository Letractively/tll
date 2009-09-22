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
 * CustomerListingConfig
 * @author jpk
 */
public class IspListingConfig extends AccountListingConfig {

	private static final String listingElementName = SmbizEntityType.CUSTOMER.getName();

	private static final Sorting defaultSorting = new Sorting("name");

	private static final Column[] cols = new Column[] {
		Column.ROW_COUNT_COLUMN,
		new Column("Name", Model.NAME_PROPERTY, "i"),
		new Column("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "i"),
		new Column("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "i"),
		new Column("Status", "status", "i"),
		new Column("Billing Model", "billingModel", "i"),
		new Column("Billing Cycle", "billingCycle", "i"),
	};

	private static final String[] mprops = new String[] {
		Model.NAME_PROPERTY,
		Model.DATE_CREATED_PROPERTY,
		Model.DATE_MODIFIED_PROPERTY,
		"status",
		"billingModel",
		"billingCycle",
	};

	/**
	 * Constructor
	 */
	public IspListingConfig() {
		super(null, listingElementName, mprops, cols, defaultSorting);
	}

	@Override
	public int getPageSize() {
		return 2; // TODO for testing
	}
}
