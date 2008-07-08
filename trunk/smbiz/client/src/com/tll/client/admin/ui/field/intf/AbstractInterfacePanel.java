/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.widgetideas.table.client.overrides.Grid;
import com.tll.client.field.FieldGroup;
import com.tll.client.listing.Column;
import com.tll.client.model.PropertyPath;
import com.tll.client.ui.field.AbstractField;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.field.VerticalFieldPanelComposer;

/**
 * AbstractInterfacePanel
 * @author jpk
 */
public abstract class AbstractInterfacePanel extends FieldGroupPanel {

	protected static final Column[] paramColumns = new Column[] {
		new Column("Name", "name"),
		new Column("Code", "code"),
		new Column("Description", "description") };

	final class ParamFieldRenderer implements IFieldRenderer {

		private final String paramPropName;
		private final FieldGroup fieldGroup;

		/**
		 * Constructor
		 * @param fieldGroup The group containing the paremeter fields
		 * @param paramPropName The related many parameters property name
		 */
		public ParamFieldRenderer(FieldGroup fieldGroup, String paramPropName) {
			super();
			this.fieldGroup = fieldGroup;
			this.paramPropName = paramPropName;
		}

		public void draw(Panel canvas) {
			VerticalFieldPanelComposer cmpsr = new VerticalFieldPanelComposer();
			cmpsr.setCanvas(canvas);

			final PropertyPath pp = new PropertyPath(paramPropName);
			final int depth = pp.depth();

			pp.append("name");
			cmpsr.addField((AbstractField) fieldGroup.getField(pp.toString()));

			pp.replaceAt(depth, "code");
			cmpsr.addField((AbstractField) fieldGroup.getField(pp.toString()));

			pp.replaceAt(depth, "description");
			cmpsr.addField((AbstractField) fieldGroup.getField(pp.toString()));
		}

	}

	protected static final IFieldRenderer paramFieldRenderer = new IFieldRenderer() {

		public void draw(Panel canvas) {
			VerticalFieldPanelComposer cmpsr = new VerticalFieldPanelComposer();
			cmpsr.setCanvas(canvas);
		}
	};

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
