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
public class CustomerListingConfig extends AccountListingConfig {

	private static final String listingElementName = SmbizEntityType.CUSTOMER.getName();

	private static final Sorting defaultSorting = new Sorting("name");

	private static final Column[] cols = new Column[] {
		Column.ROW_COUNT_COLUMN,
		new Column("Name", Model.NAME_PROPERTY, "c"),
		new Column("Created", GlobalFormat.DATE, Model.DATE_CREATED_PROPERTY, "ca"),
		new Column("Modified", GlobalFormat.DATE, Model.DATE_MODIFIED_PROPERTY, "ca"),
		new Column("Status", "status", "ca"),
		new Column("Billing Model", "billingModel", "ca"),
		new Column("Billing Cycle", "billingCycle", "ca"),
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
	public CustomerListingConfig() {
		super(null, listingElementName, mprops, cols, defaultSorting);
	}
}
