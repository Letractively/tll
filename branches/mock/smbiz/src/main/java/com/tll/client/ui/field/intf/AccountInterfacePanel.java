/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.ui.field.intf;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.tll.client.Style;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.Br;
import com.tll.client.ui.Heading;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
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
import com.tll.model.InterfaceStatus;

/**
 * AccountInterfacePanel - Displays account interface option data for a single
 * interface.
 * @author jpk
 */
public class AccountInterfacePanel extends FlowFieldPanel {

	static class InterfaceSummary extends Composite {

		final FlowPanel pnl = new FlowPanel();
		final Grid desc;
		final VerticalPanel availability;

		/**
		 * Constructor
		 */
		public InterfaceSummary() {
			super();

			// desc grid
			desc = new Grid(3, 2);
			pnl.add(desc);
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

			// availablity grid
			availability = new VerticalPanel();
			pnl.add(availability);
			Label l;
			l = new Label("Availability");
			l.addStyleName(FieldPanel.Styles.FIELD_TITLE);
			availability.add(l);
			availability.add(new Label("")); // isAvailableAsp
			availability.add(new Label("")); // isAvailableIsp
			availability.add(new Label("")); // isAvailableMerchant
			availability.add(new Label("")); // isAvailableCustomer
			availability.add(new Br());
			l = new Label("Requiredness");
			l.addStyleName(FieldPanel.Styles.FIELD_TITLE);
			availability.add(l);
			availability.add(new Label("")); // isRequiredAsp
			availability.add(new Label("")); // isRequiredIsp
			availability.add(new Label("")); // isRequiredMerchant
			availability.add(new Label("")); // isRequiredCustomer

			initWidget(pnl);
		}

		void apply(Model intf) {
			desc.setText(0, 1, intf.asString("name"));
			desc.setText(1, 1, intf.asString("code"));
			desc.setText(2, 1, intf.asString("desciption"));

			// TODO finish
			// availability.getWidget(index)

		}

	} // InterfaceSummary

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
			// desc
			desc.setText(0, 1, option.asString("name"));
			desc.setText(1, 1, option.asString("code"));
			desc.setText(2, 1, option.asString("desciption"));
			desc.setText(3, 1, option.asString("default").equals("true") ? "Yes" : "No");

			// pricing
			DoublePropertyValue dpv;
			String price;
			try {
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
	 * AccountOptionPanel - Manages the interface option to account
	 * bindings for a given interface option and account.
	 * @author jpk
	 */
	static class AccountOptionPanel extends FlowFieldPanel {

		/**
		 * AccountParameterPanel - Represents an interface option parameter that
		 * handles account subscribing and un-subscribing.
		 * @author jpk
		 */
		class AccountParameterPanel extends FlowFieldPanel {

			CheckboxField fSubscribed;
			Label lblPName, lblPDesc;
			TextField fPValue;

			@Override
			protected FieldGroup generateFieldGroup() {
				final FieldGroup fg = new FieldGroup("Account Parameters");
				fg.addField(fSubscribed);
				fg.addField(fPValue);
				return fg;
				/*
				lblPName = new Label("", false);
				lblPDesc = new Label("", false);
				setModel(iopd);

				final String op = PropertyPath.index("options", optionIndex);
				fSubscribed = FieldFactory.fcheckbox("subscribed", op + ".subscribed", "Subscribed", "Subscribed");
				fSubscribed.setValue(subscribed);
				fPValue = FieldFactory.ftext("pvalue", op + ".value", "Value", "Value", 20);
				fPValue.setValue(paramValue);
				 */
			}

			@Override
			protected IFieldRenderer<FlowPanel> getRenderer() {
				return new IFieldRenderer<FlowPanel>() {

					@Override
					public void render(FlowPanel widget, FieldGroup fg) {
						final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
						cmpsr.setCanvas(widget);

						cmpsr.addWidget("Name", lblPName);
						cmpsr.addWidget("Description", lblPDesc);
						cmpsr.addField(fSubscribed);
						cmpsr.addField(fPValue);
					}
				};
			}

			@Override
			public void setModel(Model iopd) {
				super.setModel(iopd);
				lblPName.setText(iopd.asString("name"));
				lblPDesc.setText(iopd.asString("description"));
			}

		} // AccountParameterPanel

		/**
		 * ParametersPanel - Renders a collection of {@link AccountParameterPanel}
		 * panels.
		 * @author jpk
		 */
		class ParametersPanel extends TabbedIndexedFieldPanel<AccountParameterPanel> {

			/**
			 * Constructor
			 */
			public ParametersPanel() {
				super("Parameters", "parameters", false, false);
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

		/**
		 * FieldProvider
		 * @author jpk
		 */
		class FieldProvider extends AbstractFieldGroupProvider {

			@Override
			protected String getFieldGroupName() {
				return "Account Interface Options";
			}

			@Override
			protected void populateFieldGroup(FieldGroup fg) {
				fg.addField(fenumselect("status", "status", "Status", "Status", InterfaceStatus.class));
				fg.addField(ftext("setUpPrice", "setUpPrice", "Set Up Price", "Set Up Price", 8));
				fg.addField(ftext("monthlyPrice", "monthlyPrice", "Monthly Price", "Monthly Price", 8));
				fg.addField(ftext("annualPrice", "annualPrice", "Annual Price", "Annual Price", 8));
			}
		} // FieldProvider

		final InterfaceOptionSummary ioSmry = new InterfaceOptionSummary();
		final ParametersPanel paramWidget = new ParametersPanel();

		@Override
		protected FieldGroup generateFieldGroup() {
			return new FieldProvider().getFieldGroup();
		}

		@Override
		protected IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel widget, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(widget);

					cmpsr.addWidget(ioSmry);
					cmpsr.addField(fg.getFieldWidget("setUpPrice"));
					cmpsr.addField(fg.getFieldWidget("monthlyPrice"));
					cmpsr.addField(fg.getFieldWidget("annualPrice"));
					cmpsr.addWidget("Parameters", paramWidget);
				}
			};
		}

		@Override
		public void setModel(Model model) {
			super.setModel(model);
			ioSmry.apply(model);
		}

	} // AccountOptionPanel

	final InterfaceSummary iSmry = new InterfaceSummary();
	final ArrayList<AccountOptionPanel> options = new ArrayList<AccountOptionPanel>();

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = new FieldGroup("Account Interface Options");
		// fg.addField(FieldFactory.)
		return fg;
	}

	@Override
	protected IFieldRenderer<FlowPanel> getRenderer() {
		return null;
	}

	@Override
	public void setModel(Model model) {
		super.setModel(model);

	}

}
