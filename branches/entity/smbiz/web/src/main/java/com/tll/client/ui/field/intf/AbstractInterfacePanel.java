/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.Br;
import com.tll.client.ui.field.AbstractBindableFieldPanel;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.AbstractBindableFlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;

/**
 * AbstractInterfacePanel - Base class for {@link AbstractBindableFieldPanel}s that display app
 * interfaces (not Options).
 * @author jpk
 */
abstract class AbstractInterfacePanel extends AbstractBindableFlowFieldPanel {

	/**
	 * OptionRenderer
	 * @author jpk
	 */
	static class OptionRenderer implements IFieldRenderer<FlowPanel> {

		private final boolean isSwitch;
		private final Widget paramsWidget;
		private final FlowPanelFieldComposer composer;

		/**
		 * Constructor
		 * @param isSwitch
		 * @param paramsWidget
		 * @param composer
		 */
		public OptionRenderer(boolean isSwitch, Widget paramsWidget, FlowPanelFieldComposer composer) {
			super();
			this.isSwitch = isSwitch;
			this.paramsWidget = paramsWidget;
			this.composer = composer;
		}

		@Override
		public void render(FlowPanel widget, FieldGroup fg) {
			if(!isSwitch) {
				composer.addField(fg.getFieldWidget("optn" + Model.NAME_PROPERTY));
				composer.addField(fg.getFieldWidget("optnCode"));
				composer.addField(fg.getFieldWidget("optnDefault"));
				composer.newRow();
				composer.addField(fg.getFieldWidget("optnDescription"));
				composer.newRow();
			}

			// pricing
			final FlowPanel fp = new FlowPanel();
			final GridFieldComposer pc = new GridFieldComposer();
			pc.setCanvas(fp);

			pc.addFieldTitle("Cost");
			pc.addField(fg.getFieldWidget("optnSetUpCost"));
			pc.addField(fg.getFieldWidget("optnMonthlyCost"));
			pc.addField(fg.getFieldWidget("optnAnnualCost"));

			pc.addFieldTitle("Pricing");
			pc.addField(fg.getFieldWidget("optnBaseSetupPrice"));
			pc.addField(fg.getFieldWidget("optnBaseMonthlyPrice"));
			pc.addField(fg.getFieldWidget("optnBaseAnnualPrice"));

			fp.addStyleName(Style.GAP_LEFT);
			composer.addWidget(fp);

			paramsWidget.addStyleName(Style.GAP_LEFT);
			composer.addWidget(paramsWidget);
		}

	} // OptionRenderer

	/**
	 * ParamsPanel
	 * @author jpk
	 */
	static final class ParamsPanel extends TabbedIndexedFieldPanel<ParamsPanel.ParamPanel> {

		/**
		 * ParamPanel
		 * @author jpk
		 */
		static final class ParamPanel extends AbstractBindableFlowFieldPanel {

			@Override
			protected FieldGroup generateFieldGroup() {
				return (new ParamDefFieldProvider()).getFieldGroup();
			}

			@Override
			protected IFieldRenderer<FlowPanel> getRenderer() {
				return new IFieldRenderer<FlowPanel>() {

					@Override
					public void render(FlowPanel widget, FieldGroup fg) {
						final GridFieldComposer cmpsr = new GridFieldComposer();
						cmpsr.setCanvas(widget);

						cmpsr.addField(fg.getFieldWidget("param" + Model.NAME_PROPERTY));
						cmpsr.addField(fg.getFieldWidget("paramCode"));
						cmpsr.addField(fg.getFieldWidget("paramDescription"));
					}
				};
			}

		}

		/**
		 * Constructor
		 * @param indexPropPath
		 */
		public ParamsPanel(String indexPropPath) {
			super("Params", indexPropPath, true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Param";
		}

		@Override
		protected String getInstanceName(ParamPanel index) {
			return index.getFieldGroup().getFieldWidget("param" + Model.NAME_PROPERTY).getText();
		}

		@Override
		protected Model createPrototypeModel() {
			return ModelAssembler.assemble(SmbizEntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
		}

		@Override
		protected ParamPanel createIndexPanel() {
			return new ParamPanel();
		}

	} // ParamsPanel

	protected static Widget createAvailabilityWidget(FieldGroup fg) {
		final VerticalPanel g = new VerticalPanel();
		Label l;
		l = new Label("Availability");
		l.addStyleName(com.tll.client.ui.field.IFieldWidget.Styles.FIELD_TITLE);
		g.add(l);
		g.add(fg.getFieldWidget("intfIsAvailableAsp").getWidget());
		g.add(fg.getFieldWidget("intfIsAvailableIsp").getWidget());
		g.add(fg.getFieldWidget("intfIsAvailableMerchant").getWidget());
		g.add(fg.getFieldWidget("intfIsAvailableCustomer").getWidget());
		g.add(new Br());
		l = new Label("Requiredness");
		l.addStyleName(com.tll.client.ui.field.IFieldWidget.Styles.FIELD_TITLE);
		g.add(l);
		g.add(fg.getFieldWidget("intfIsRequiredAsp").getWidget());
		g.add(fg.getFieldWidget("intfIsRequiredIsp").getWidget());
		g.add(fg.getFieldWidget("intfIsRequiredMerchant").getWidget());
		g.add(fg.getFieldWidget("intfIsRequiredCustomer").getWidget());
		return g;
	}

	static class InterfaceFieldProvider extends AbstractFieldGroupProvider {

		@Override
		protected String getFieldGroupName() {
			return "Interface";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			addModelCommon(fg, true, true, "intf");
			fg.addField(ftext("intfCode", "code", "Code", "Code", 20));
			fg.addField(ftextarea("intfDescription", "description", "Description", "Description", 3, 25));

			fg.addField(fcheckbox("intfIsAvailableAsp", "isAvailableAsp", "Asp", "Available to the Asp?"));
			fg.addField(fcheckbox("intfIsAvailableIsp", "isAvailableIsp", "Isp", "Available to Isps?"));
			fg
			.addField(fcheckbox("intfIsAvailableMerchant", "isAvailableMerchant", "Merchant",
			"Available to the Merchants"));
			fg
			.addField(fcheckbox("intfIsAvailableCustomer", "isAvailableCustomer", "Customer",
			"Available to the Customers"));

			fg.addField(fcheckbox("intfIsRequiredAsp", "isRequiredAsp", "Asp", "Required by the Asp?"));
			fg.addField(fcheckbox("intfIsRequiredIsp", "isRequiredIsp", "Isp", "Required to Isps?"));
			fg.addField(fcheckbox("intfIsRequiredMerchant", "isRequiredMerchant", "Merchant", "Required to Merchants?"));
			fg.addField(fcheckbox("intfIsRequiredCustomer", "isRequiredCustomer", "Customer", "Required to Customers?"));
		}
	}

	static class OptionFieldProvider extends AbstractFieldGroupProvider {

		private final boolean isSwitch;

		/**
		 * Constructor
		 * @param isSwitch
		 */
		public OptionFieldProvider(boolean isSwitch) {
			super();
			this.isSwitch = isSwitch;
		}

		@Override
		protected String getFieldGroupName() {
			return "Option";
		}

		@Override
		public void populateFieldGroup(FieldGroup fg) {
			if(!isSwitch) {
				addModelCommon(fg, true, false, "optn");
				fg.addField(ftext("optnCode", "code", "Code", "Code", 20));
				fg.addField(ftextarea("optnDescription", "description", "Description", "Description", 3, 25));
			}
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
			addModelCommon(fg, true, false, "param");
			fg.addField(ftext("paramCode", "code", "Code", "Code", 20));
			fg.addField(ftextarea("paramDescription", "description", "Description", "Description", 3, 25));
		}

	}
}
