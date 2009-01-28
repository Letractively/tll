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
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;

/**
 * AbstractInterfacePanel - Base class for {@link FieldPanel}s that display app
 * interfaces (not Options).
 * @param <W> The wrapped widget and field rendering widget type
 * @param <M> the model type
 * @author jpk
 */
public abstract class AbstractInterfacePanel<W extends Widget, M extends IBindable> extends FieldPanel<W, M> {

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
			fg.addField(fstext("intfCode", "code", "Code", "Code", 20));
			fg.addField(FieldFactory.ftextarea("intfDescription", "description", "Desc", "Description", 3, 8));

			fg.addField(fbool("intfIsAvailableAsp", "isAvailableAsp", null, null));
			fg.addField(fbool("intfIsAvailableIsp", "isAvailableIsp", null, null));
			fg.addField(fbool("intfIsAvailableMerchant", "isAvailableMerchant", null, null));
			fg.addField(fbool("intfIsAvailableCustomer", "isAvailableCustomer", null, null));

			fg.addField(fbool("intfIsRequiredAsp", "isRequiredAsp", null, null));
			fg.addField(fbool("intfIsRequiredIsp", "isRequiredIsp", null, null));
			fg.addField(fbool("intfIsRequiredMerchant", "isRequiredMerchant", null, null));
			fg.addField(fbool("intfIsRequiredCustomer", "isRequiredCustomer", null, null));
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
			fg.addField(fstext("optnCode", "code", "Code", "Code", 20));
			fg.addField(FieldFactory.ftextarea("description", "description", "Desc", "Description", 3, 8));

			fg.addField(fmoney("optnSetUpCost", "setUpCost", "Set Up", "Set Up Cost", 8));
			fg.addField(fmoney("optnMonthlyCost", "monthlyCost", "Monthly", "Monthly Cost", 8));
			fg.addField(fmoney("optnAnnualCost", "annualCost", "Annual", "Annual Cost", 8));

			fg.addField(fmoney("optnBaseSetupPrice", "baseSetupPrice", "Set Up", "Base Set Up Price", 8));
			fg.addField(fmoney("optnBaseMonthlyPrice", "baseMonthlyPrice", "Monthly", "Base Monthly Price", 8));
			fg.addField(fmoney("optnBaseAnnualPrice", "baseAnnualPrice", "Annual", "Base Annual Price", 8));
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
			fg.addField(fstext("paramCode", "code", "Code", "Code", 20));
			fg.addField(FieldFactory.ftextarea("paramDescription", "description", "Desc", "Description", 3, 8));
		}

	}

}
