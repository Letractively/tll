/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.tll.client.bind.IBindable;
import com.tll.client.listing.Column;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * AbstractInterfacePanel - Base class for {@link FieldPanel}s that display app
 * interfaces (not Options).
 * @author jpk
 */
public abstract class AbstractInterfacePanel<M extends IBindable> extends FieldPanel<M> {

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
		super();
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		name = FieldFactory.entityNameField();
		code = FieldFactory.ftext("code", "Code", "Code", 20);
		description = FieldFactory.ftextarea("description", "Desc", "Description", 3, 8);
		timestamps = FieldFactory.entityTimestampFields();

		isAvailableAsp = FieldFactory.fcheckbox("isAvailableAsp", null, null);
		isAvailableIsp = FieldFactory.fcheckbox("isAvailableIsp", null, null);
		isAvailableMerchant = FieldFactory.fcheckbox("isAvailableMerchant", null, null);
		isAvailableCustomer = FieldFactory.fcheckbox("isAvailableCustomer", null, null);

		isRequiredAsp = FieldFactory.fcheckbox("isRequiredAsp", null, null);
		isRequiredIsp = FieldFactory.fcheckbox("isRequiredIsp", null, null);
		isRequiredMerchant = FieldFactory.fcheckbox("isRequiredMerchant", null, null);
		isRequiredCustomer = FieldFactory.fcheckbox("isRequiredCustomer", null, null);

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
