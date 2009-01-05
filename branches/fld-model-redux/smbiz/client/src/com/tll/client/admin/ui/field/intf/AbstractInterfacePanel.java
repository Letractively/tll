/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.IFieldGroupProvider;

/**
 * AbstractInterfacePanel - Base class for {@link FieldPanel}s that display app
 * interfaces (not Options).
 * @author jpk
 */
public abstract class AbstractInterfacePanel<M extends IBindable> extends FieldPanel<M> {

	protected static Grid createAvailabilityGrid(FieldGroup fg) {
		Grid g = new Grid(3, 5);
		g.setWidget(0, 1, new Label("Asp"));
		g.setWidget(0, 2, new Label("Isp"));
		g.setWidget(0, 3, new Label("Mrc"));
		g.setWidget(0, 4, new Label("Cst"));
		g.setWidget(1, 0, new Label("Available?"));
		g.setWidget(1, 1, (Widget) fg.getField("isAvailableAsp"));
		g.setWidget(1, 2, (Widget) fg.getField("isAvailableIsp"));
		g.setWidget(1, 3, (Widget) fg.getField("isAvailableMerchant"));
		g.setWidget(1, 4, (Widget) fg.getField("isAvailableCustomer"));
		g.setWidget(2, 0, new Label("Required?"));
		g.setWidget(2, 1, (Widget) fg.getField("isRequiredAsp"));
		g.setWidget(2, 2, (Widget) fg.getField("isRequiredIsp"));
		g.setWidget(2, 3, (Widget) fg.getField("isRequiredMerchant"));
		g.setWidget(2, 4, (Widget) fg.getField("isRequiredCustomer"));
		return g;
	}

	/*
	protected static final Column[] paramColumns = new Column[] {
		new Column("Name", "name"), new Column("Code", "code"), new Column("Description", "description") };

	protected TextField name, code;
	protected TextAreaField description;
	protected DateField[] timestamps;
	protected CheckboxField isAvailableAsp, isAvailableIsp, isAvailableMerchant, isAvailableCustomer;
	protected CheckboxField isRequiredAsp, isRequiredIsp, isRequiredMerchant, isRequiredCustomer;
	*/

	/**
	 * Constructor
	 */
	public AbstractInterfacePanel() {
		super();
	}

	class InterfaceFieldProvider implements IFieldGroupProvider {

		protected void pouplateFieldGroup(FieldGroup fields) {
			fields.addField(FieldFactory.entityNameField());
			fields.addField(FieldFactory.ftext("code", "Code", "Code", 20));
			fields.addField(FieldFactory.ftextarea("description", "Desc", "Description", 3, 8));
			fields.addFields(FieldFactory.entityTimestampFields());

			fields.addField(FieldFactory.fcheckbox("isAvailableAsp", null, null));
			fields.addField(FieldFactory.fcheckbox("isAvailableIsp", null, null));
			fields.addField(FieldFactory.fcheckbox("isAvailableMerchant", null, null));
			fields.addField(FieldFactory.fcheckbox("isAvailableCustomer", null, null));

			fields.addField(FieldFactory.fcheckbox("isRequiredAsp", null, null));
			fields.addField(FieldFactory.fcheckbox("isRequiredIsp", null, null));
			fields.addField(FieldFactory.fcheckbox("isRequiredMerchant", null, null));
			fields.addField(FieldFactory.fcheckbox("isRequiredCustomer", null, null));
		}

		public FieldGroup getFieldGroup() {
			FieldGroup fields = new FieldGroup();
			pouplateFieldGroup(fields);
			return fields;
		}
	}
}
