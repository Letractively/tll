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
 * AccountInterfaceOption - Manages the interface option to account bindings for
 * a given interface option and account.
 * @author jpk
 */
public class AccountInterfaceOption extends FlowFieldPanel {

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
	 * OptionParameter - Represents an interface option parameter that handles
	 * account subscribing and un-subscribing.
	 * @author jpk
	 */
	class OptionParameter extends FlowFieldPanel {

		final int optionIndex;
		CheckboxField fSubscribed;
		Label lblPName, lblPDesc;
		TextField fPValue;

		/**
		 * Constructor
		 * @param optionIndex
		 */
		public OptionParameter(int optionIndex) {
			super();
			this.optionIndex = optionIndex;
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new AbstractFieldGroupProvider() {

				@Override
				protected void populateFieldGroup(FieldGroup fg) {
					final String op = PropertyPath.index("options", optionIndex);
					fSubscribed = fcheckbox("subscribed", op + ".subscribed", "Subscribed", "Subscribed");
					fPValue = ftext("pvalue", op + ".value", "Value", "Value", 20);
				}

				@Override
				protected String getFieldGroupName() {
					return "Option Parameter";
				}
			}).getFieldGroup();
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
		public void setModel(Model model) {
			super.setModel(model);
			// update the static label text

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
			Map<String, String> pmap;
			try {
				pmap = ((StringMapPropertyValue) ioa.getPropertyValue("parameters")).getStringMap();
				final Model option = ioa.getNestedModel("option");
				final List<Model> iopds = option.relatedMany("parameters").getList();
				for(final Model iopd : iopds) {
					final String pname = iopd.asString("name");
					if(pmap.containsKey(pname)) {
						// "subscribed"

					}
					else {
						// "un-subscribed"

					}
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
	final Parameters params = new Parameters();

	@Override
	public void setModel(Model ioa) {
		// assert model.getEntityType() == SmbizEntityType.INTERFACE_OPTION_ACCOUNT;

		try {
			final Model option = ioa.getNestedModel("option");

			// populate the option static data
			ioSmry.apply(option);

			// create the ioa param fields
			params.apply(ioa);
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
			}
		};
	}

}
