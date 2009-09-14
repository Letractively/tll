/**
 * The Logic Lab
 * @author jpk
 * @since Sep 11, 2009
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.tll.client.Style;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.Heading;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.Fmt;
import com.tll.common.model.DoublePropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.SmbizEntityType;

/**
 * AbstractAccountInterfacePanel
 * @author jpk
 */
abstract class AbstractAccountInterfacePanel extends FlowFieldPanel {

	/**
	 * InterfaceOptionSummary - Static text that summarizes an interface option.
	 * @author jpk
	 */
	static class InterfaceOptionSummary extends Composite {

		static final String[][] arrBasePricing = new String[][] {
			{
				"baseSetupPrice", "Setup" }, {
					"baseMonthlyPrice", "Monthly" }, {
						"baseAnnualPrice", "Annual" } };

		final FlowPanel pnl = new FlowPanel();
		final Grid desc, pricing;

		/**
		 * Constructor
		 */
		public InterfaceOptionSummary() {
			Heading h;
			h = new Heading(3, "Option Summary");
			pnl.add(h);

			// desc grid
			desc = new Grid(4, 2);
			Label lbl;
			lbl = new Label("Name", false);
			lbl.addStyleName(Style.BOLD);
			desc.setWidget(0, 0, lbl);
			lbl = new Label("Code", false);
			lbl.addStyleName(Style.BOLD);
			desc.setWidget(1, 0, lbl);
			lbl = new Label("Description", false);
			lbl.addStyleName(Style.BOLD);
			desc.setWidget(2, 0, lbl);
			lbl = new Label("Default?", false);
			lbl.addStyleName(Style.BOLD);
			desc.setWidget(3, 0, lbl);
			pnl.add(desc);

			// pricing grid
			pricing = new Grid(3, 2);

			h = new Heading(4, "Base Pricing");
			pnl.add(h);
			for(int i = 0; i < arrBasePricing.length; i++) {
				pricing.setText(i, 0, arrBasePricing[i][1]);
			}
			pnl.add(pricing);
			initWidget(pnl);
		}

		public void apply(Model option) {
			try {
				// desc
				desc.setText(0, 1, option.asString("name"));
				desc.setText(1, 1, option.asString("code"));
				desc.setText(2, 1, option.asString("description"));
				desc.setText(3, 1, option.getProperty("default") == Boolean.TRUE ? "Yes" : "No");

				// pricing
				DoublePropertyValue dpv;
				String price;
				for(int i = 0; i < arrBasePricing.length; i++) {
					dpv = (DoublePropertyValue) option.getModelProperty(arrBasePricing[i][0]);
					price = Fmt.currency(dpv.getDouble());
					pricing.setText(i, 1, price);
				}
			}
			catch(final PropertyPathException e) {
				throw new IllegalArgumentException(e);
			}
		}
	} // InterfaceOptionSummary

	/**
	 * AccountParameterPanel - Represents an interface option parameter that
	 * handles account subscribing and un-subscribing.
	 * @author jpk
	 */
	static class AccountParameterPanel extends FlowFieldPanel {

		final Label lblName, lblCode, lblDesc;
		TextField value;

		public AccountParameterPanel() {
			lblName = new Label();
			lblCode = new Label();
			lblDesc = new Label();
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			final FieldGroup fg = new FieldGroup("Account Parameters");

			value = FieldFactory.ftext("value", "value", "Value", "Parameter value", 10);
			fg.addField(value);

			return fg;
		}

		@Override
		protected IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel widget, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(widget);

					cmpsr.addWidget("Name", lblName);
					cmpsr.addWidget("Code", lblCode);
					cmpsr.addWidget("Description", lblDesc);
					cmpsr.addField(value);
				}
			};
		}

		@Override
		public void setModel(Model aio) {
			super.setModel(aio);
			lblName.setText(aio.asString("name"));
			lblName.setText(aio.asString("code"));
			lblDesc.setText(aio.asString("description"));
		}

	} // AccountParameterPanel

	/**
	 * AccountParamsPanel - Renders a collection of
	 * {@link AbstractAccountInterfacePanel.AccountParameterPanel} panels.
	 * @author jpk
	 */
	static class AccountParamsPanel extends TabbedIndexedFieldPanel<AccountParameterPanel> {

		/**
		 * Constructor
		 * @param indexPropPath
		 */
		public AccountParamsPanel(String indexPropPath) {
			super("Parameters", indexPropPath, false, false);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Option Parameter";
		}

		@Override
		protected String getInstanceName(AccountParameterPanel index) {
			String pname;
			try {
				pname = (String) index.getModel().getProperty("name");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}
			return pname;
		}

		@Override
		protected AccountParameterPanel createIndexPanel() {
			return new AccountParameterPanel();
		}

		@Override
		protected Model createPrototypeModel() {
			return ModelAssembler.assemble(SmbizEntityType.ACCOUNT_INTERFACE_OPTION_PARAMETER);
		}

	} // ParametersPanel
}
