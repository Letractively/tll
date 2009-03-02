/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.bind.IBindable;

/**
 * AbstractInterfacePanel - Base class for {@link FieldPanel}s that display app
 * interfaces (not Options).
 * @param <W> The wrapped widget and field rendering widget type
 * @param <M> the model type
 * @author jpk
 */
public abstract class AbstractInterfacePanel<W extends Widget, M extends IBindable> extends FieldPanel<W> {

	protected static Grid createAvailabilityGrid(FieldGroup fg) {
		final Grid g = new Grid(3, 5);
		g.setWidget(0, 1, new Label("Asp"));
		g.setWidget(0, 2, new Label("Isp"));
		g.setWidget(0, 3, new Label("Mrc"));
		g.setWidget(0, 4, new Label("Cst"));
		g.setWidget(1, 0, new Label("Available?"));
		g.setWidget(1, 1, (Widget) fg.getFieldWidget("isAvailableAsp"));
		g.setWidget(1, 2, (Widget) fg.getFieldWidget("isAvailableIsp"));
		g.setWidget(1, 3, (Widget) fg.getFieldWidget("isAvailableMerchant"));
		g.setWidget(1, 4, (Widget) fg.getFieldWidget("isAvailableCustomer"));
		g.setWidget(2, 0, new Label("Required?"));
		g.setWidget(2, 1, (Widget) fg.getFieldWidget("isRequiredAsp"));
		g.setWidget(2, 2, (Widget) fg.getFieldWidget("isRequiredIsp"));
		g.setWidget(2, 3, (Widget) fg.getFieldWidget("isRequiredMerchant"));
		g.setWidget(2, 4, (Widget) fg.getFieldWidget("isRequiredCustomer"));
		return g;
	}

	/*
	protected static final Column[] paramColumns = new Column[] {
		new Column("Name", "name"), new Column("Code", "code"), new Column("Description", "description") };
	*/

	/**
	 * Constructor
	 */
	public AbstractInterfacePanel() {
		super();
	}

	static class InterfaceFieldProvider extends AbstractFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Interface";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			addModelCommon(fg, true, true);
			fg.addField(ftext("intfCode", "code", "Code", "Code", 20));
			fg.addField(ftextarea("intfDescription", "description", "Desc", "Description", 3, 8));

			fg.addField(fcheckbox("intfIsAvailableAsp", "isAvailableAsp", null, null));
			fg.addField(fcheckbox("intfIsAvailableIsp", "isAvailableIsp", null, null));
			fg.addField(fcheckbox("intfIsAvailableMerchant", "isAvailableMerchant", null, null));
			fg.addField(fcheckbox("intfIsAvailableCustomer", "isAvailableCustomer", null, null));

			fg.addField(fcheckbox("intfIsRequiredAsp", "isRequiredAsp", null, null));
			fg.addField(fcheckbox("intfIsRequiredIsp", "isRequiredIsp", null, null));
			fg.addField(fcheckbox("intfIsRequiredMerchant", "isRequiredMerchant", null, null));
			fg.addField(fcheckbox("intfIsRequiredCustomer", "isRequiredCustomer", null, null));
		}
	}

	static class OptionFieldProvider extends AbstractFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Option";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			addModelCommon(fg, true, false);
			fg.addField(ftext("optnCode", "code", "Code", "Code", 20));
			fg.addField(ftextarea("description", "description", "Desc", "Description", 3, 8));

			fg.addField(ftext("optnSetUpCost", "setUpCost", "Set Up", "Set Up Cost", 8));
			fg.addField(ftext("optnMonthlyCost", "monthlyCost", "Monthly", "Monthly Cost", 8));
			fg.addField(ftext("optnAnnualCost", "annualCost", "Annual", "Annual Cost", 8));

			fg.addField(ftext("optnBaseSetupPrice", "baseSetupPrice", "Set Up", "Base Set Up Price", 8));
			fg.addField(ftext("optnBaseMonthlyPrice", "baseMonthlyPrice", "Monthly", "Base Monthly Price", 8));
			fg.addField(ftext("optnBaseAnnualPrice", "baseAnnualPrice", "Annual", "Base Annual Price", 8));
		}

	}

	static class ParamDefFieldProvider extends AbstractFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Parameter";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			addModelCommon(fg, true, false);
			fg.addField(ftext("paramCode", "code", "Code", "Code", 20));
			fg.addField(ftextarea("paramDescription", "description", "Desc", "Description", 3, 8));
		}

	}

}
