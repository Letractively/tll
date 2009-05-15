/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.ui.field.intf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.Style;
import com.tll.client.ui.Heading;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.Fmt;
import com.tll.common.model.DoublePropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.PropPathNodeMismatchException;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.StringMapPropertyValue;
import com.tll.model.InterfaceStatus;
import com.tll.util.PropertyPath;

/**
 * AccountInterfaceOptions - Displays account interface option data for a single
 * interface.
 * @author jpk
 */
public class AccountInterfaceOptions extends FlowFieldPanel {

	/**
	 * InterfaceSummary - Static text that summarizes an interface option.
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
			// default?
			final boolean isDefault = option.asString("default").equals("true");
			desc.setText(2, 1, isDefault ? "Yes" : "No");

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
	 * AccountInterfaceOption - Manages the interface option to account bindings
	 * for a given interface option and account.
	 * @author jpk
	 */
	class AccountInterfaceOption extends FlowFieldPanel {

		/**
		 * OptionParameter - Represents an interface option parameter that handles
		 * account subscribing and un-subscribing.
		 * @author jpk
		 */
		class OptionParameter extends FlowFieldPanel {

			final int optionIndex;
			final CheckboxField fSubscribed;
			final Label lblPName, lblPDesc;
			final TextField fPValue;

			/**
			 * Constructor
			 * @param optionIndex
			 * @param iopd
			 * @param subscribed
			 * @param paramValue
			 */
			public OptionParameter(int optionIndex, Model iopd, boolean subscribed, String paramValue) {
				super();
				this.optionIndex = optionIndex;

				lblPName = new Label("", false);
				lblPDesc = new Label("", false);
				setModel(iopd);

				final String op = PropertyPath.index("options", optionIndex);
				fSubscribed = FieldFactory.fcheckbox("subscribed", op + ".subscribed", "Subscribed", "Subscribed");
				fSubscribed.setValue(subscribed);
				fPValue = FieldFactory.ftext("pvalue", op + ".value", "Value", "Value", 20);
				fPValue.setValue(paramValue);
			}

			@Override
			protected FieldGroup generateFieldGroup() {
				final FieldGroup fg = new FieldGroup("Interface Parameters");
				fg.addField(fSubscribed);
				fg.addField(fPValue);
				return fg;
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

		} // OptionParameter

		/**
		 * Parameters - Renders a collection of {@link OptionParameter} panels.
		 * @author jpk
		 */
		class Parameters extends Composite {

			private final TabPanel tp = new TabPanel();
			final ArrayList<OptionParameter> params = new ArrayList<OptionParameter>();

			/**
			 * Constructor
			 */
			public Parameters() {
				initWidget(tp);
			}

			public void apply(Model ioa) {
				params.clear();
				Map<String, String> pmap;
				try {
					pmap = ((StringMapPropertyValue) ioa.getPropertyValue("parameters")).getStringMap();
					final Model option = ioa.getNestedModel("option");
					final List<Model> iopds = option.relatedMany("parameters").getList();
					int i = 0;
					for(final Model iopd : iopds) {
						final String pname = iopd.asString("name");

						final OptionParameter op = new OptionParameter(i++, iopd, pmap.containsKey(pname), pmap.get(pname));
						params.add(op);
						tp.add(op, pname);
					}
				}
				catch(final PropPathNodeMismatchException e) {
					throw new IllegalArgumentException(e);
				}
				catch(final PropertyPathException e) {
					throw new IllegalArgumentException(e);
				}
			}

		} // Parameters

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
		final Parameters paramWidget = new Parameters();

		@Override
		public void setModel(Model ioa) {
			// assert model.getEntityType() ==
			// SmbizEntityType.INTERFACE_OPTION_ACCOUNT;

			try {
				final Model option = ioa.getNestedModel("option");

				// populate the option static data
				ioSmry.apply(option);

				// create the ioa param fields
				paramWidget.apply(ioa);
			}
			catch(final PropertyPathException e) {
				throw new IllegalArgumentException(e);
			}

			super.setModel(ioa);
		}

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
	} // AccountInterfaceOption

	@Override
	protected FieldGroup generateFieldGroup() {
		return null;
	}

	@Override
	protected IFieldRenderer<FlowPanel> getRenderer() {
		return null;
	}

}