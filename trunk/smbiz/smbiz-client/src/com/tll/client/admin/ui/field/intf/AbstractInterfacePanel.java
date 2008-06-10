/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * AbstractInterfacePanel
 * @author jpk
 */
public abstract class AbstractInterfacePanel extends FieldGroupPanel {

	protected TextField name, code;
	protected TextAreaField description;
	protected DateField[] timestamps;

	protected CheckboxField isAvailableAsp;
	protected CheckboxField isAvailableIsp;
	protected CheckboxField isAvailableMerchant;
	protected CheckboxField isAvailableCustomer;

	protected CheckboxField isRequiredAsp;
	protected CheckboxField isRequiredIsp;
	protected CheckboxField isRequiredMerchant;
	protected CheckboxField isRequiredCustomer;

	/**
	 * Constructor
	 */
	public AbstractInterfacePanel() {
		super("Interface");
	}

	@Override
	public void populateFieldGroup() {
		name = createNameEntityField();
		code = ftext("code", "Code", 20);
		description = ftextarea("description", "Desc", 3, 8);
		timestamps = createTimestampEntityFields();

		isAvailableAsp = fbool("isAvailableAsp", null);
		isAvailableIsp = fbool("isAvailableIsp", null);
		isAvailableMerchant = fbool("isAvailableMerchant", null);
		isAvailableCustomer = fbool("isAvailableCustomer", null);

		isRequiredAsp = fbool("isRequiredAsp", null);
		isRequiredIsp = fbool("isRequiredIsp", null);
		isRequiredMerchant = fbool("isRequiredMerchant", null);
		isRequiredCustomer = fbool("isRequiredCustomer", null);

		addField(name);
		addField(code);
		addField(description);
		addFields(timestamps);

		addField(isAvailableAsp);
		addField(isAvailableIsp);
		addField(isAvailableMerchant);
		addField(isAvailableCustomer);
		addField(isRequiredAsp);
		addField(isRequiredIsp);
		addField(isRequiredMerchant);
		addField(isRequiredCustomer);
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
