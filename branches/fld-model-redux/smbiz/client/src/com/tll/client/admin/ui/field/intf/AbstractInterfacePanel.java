/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.tll.client.field.FieldGroup;
import com.tll.client.listing.Column;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * AbstractInterfacePanel - Base class for {@link FieldPanel}s that display app
 * interfaces (not Options).
 * @author jpk
 */
public abstract class AbstractInterfacePanel extends FieldPanel {

	protected static final Column[] paramColumns = new Column[] {
		new Column("Name", "name"), new Column("Code", "code"), new Column("Description", "description") };

	protected TextField name, code;
	protected TextAreaField description;
	protected DateField[] timestamps;
	protected CheckboxField isAvailableAsp, isAvailableIsp, isAvailableMerchant, isAvailableCustomer;
	protected CheckboxField isRequiredAsp, isRequiredIsp, isRequiredMerchant, isRequiredCustomer;

	/**
	 * Constructor
	 */
	public AbstractInterfacePanel() {
		super("Interface");
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		name = entityNameField();
		code = ftext("code", "Code", 20, null);
		description = ftextarea("description", "Desc", 3, 8, null);
		timestamps = entityTimestampFields();

		isAvailableAsp = fbool("isAvailableAsp", null, null);
		isAvailableIsp = fbool("isAvailableIsp", null, null);
		isAvailableMerchant = fbool("isAvailableMerchant", null, null);
		isAvailableCustomer = fbool("isAvailableCustomer", null, null);

		isRequiredAsp = fbool("isRequiredAsp", null, null);
		isRequiredIsp = fbool("isRequiredIsp", null, null);
		isRequiredMerchant = fbool("isRequiredMerchant", null, null);
		isRequiredCustomer = fbool("isRequiredCustomer", null, null);

		fields.addField(name);
		fields.addField(code);
		fields.addField(description);
		fields.addFields(timestamps);

		fields.addField(isAvailableAsp);
		fields.addField(isAvailableIsp);
		fields.addField(isAvailableMerchant);
		fields.addField(isAvailableCustomer);
		fields.addField(isRequiredAsp);
		fields.addField(isRequiredIsp);
		fields.addField(isRequiredMerchant);
		fields.addField(isRequiredCustomer);
	}

	protected Grid createAvailabilityGrid() {
		Grid g = new Grid(3, 5);
		g.setWidget(0, 1, new FieldLabel("Asp"));
		g.setWidget(0, 2, new FieldLabel("Isp"));
		g.setWidget(0, 3, new FieldLabel("Mrc"));
		g.setWidget(0, 4, new FieldLabel("Cst"));
		g.setWidget(1, 0, new FieldLabel("Available?"));
		g.setWidget(1, 1, isAvailableAsp);
		g.setWidget(1, 2, isAvailableIsp);
		g.setWidget(1, 3, isAvailableMerchant);
		g.setWidget(1, 4, isAvailableCustomer);
		g.setWidget(2, 0, new FieldLabel("Required?"));
		g.setWidget(2, 1, isRequiredAsp);
		g.setWidget(2, 2, isRequiredIsp);
		g.setWidget(2, 3, isRequiredMerchant);
		g.setWidget(2, 4, isRequiredCustomer);
		return g;
	}
}
