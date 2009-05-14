/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.Br;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanel;

/**
 * AbstractInterfacePanel - Base class for {@link FieldPanel}s that display app
 * interfaces (not Options).
 * @author jpk
 */
abstract class AbstractInterfacePanel extends FlowFieldPanel {

	protected static Widget createAvailabilityWidget(FieldGroup fg) {
		final VerticalPanel g = new VerticalPanel();
		Label l;
		l = new Label("Availability");
		l.addStyleName(FieldPanel.Styles.FIELD_TITLE);
		g.add(l);
		g.add(fg.getFieldWidget("isAvailableAsp").getWidget());
		g.add(fg.getFieldWidget("isAvailableIsp").getWidget());
		g.add(fg.getFieldWidget("isAvailableMerchant").getWidget());
		g.add(fg.getFieldWidget("isAvailableCustomer").getWidget());
		g.add(new Br());
		l = new Label("Requiredness");
		l.addStyleName(FieldPanel.Styles.FIELD_TITLE);
		g.add(l);
		g.add(fg.getFieldWidget("isRequiredAsp").getWidget());
		g.add(fg.getFieldWidget("isRequiredIsp").getWidget());
		g.add(fg.getFieldWidget("isRequiredMerchant").getWidget());
		g.add(fg.getFieldWidget("isRequiredCustomer").getWidget());
		return g;
	}

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
			fg.addField(ftextarea("intfDescription", "description", "Description", "Description", 3, 25));

			fg.addField(fcheckbox("intfIsAvailableAsp", "isAvailableAsp", "Asp", "Available to the Asp?"));
			fg.addField(fcheckbox("intfIsAvailableIsp", "isAvailableIsp", "Isp", "Available to Isps?"));
			fg.addField(fcheckbox("intfIsAvailableMerchant", "isAvailableMerchant", "Merchant", "Available to the Merchants"));
			fg.addField(fcheckbox("intfIsAvailableCustomer", "isAvailableCustomer", "Customer", "Available to the Customers"));

			fg.addField(fcheckbox("intfIsRequiredAsp", "isRequiredAsp", "Asp", "Required by the Asp?"));
			fg.addField(fcheckbox("intfIsRequiredIsp", "isRequiredIsp", "Isp", "Required to Isps?"));
			fg.addField(fcheckbox("intfIsRequiredMerchant", "isRequiredMerchant", "Merchant", "Required to Merchants?"));
			fg.addField(fcheckbox("intfIsRequiredCustomer", "isRequiredCustomer", "Customer", "Required to Customers?"));
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
			fg.addField(ftextarea("optnDescription", "description", "Description", "Description", 3, 25));
			fg.addField(fcheckbox("optnDefault", "default", "Default?", "Is default?"));

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
			fg.addField(ftextarea("paramDescription", "description", "Description", "Description", 3, 25));
		}

	}

}
