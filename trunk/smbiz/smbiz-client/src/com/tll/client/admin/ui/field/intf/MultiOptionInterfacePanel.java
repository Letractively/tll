/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.Model;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 */
public final class MultiOptionInterfacePanel extends AbstractInterfacePanel implements TabListener {

	private final TabPanel tabOptions = new TabPanel();

	private static final class OptionPanel extends FieldGroupPanel {

		TextField name, code;
		TextAreaField description;
		TextField[] cost, price;

		Grid params;

		/**
		 * Constructor
		 */
		public OptionPanel() {
			super("Option");
		}

		@Override
		protected void populateFieldGroup() {
			name = createNameEntityField();
			code = ftext("code", "Code", 20);
			description = ftextarea("description", "Desc", 3, 8);

			cost = new TextField[3];
			cost[0] = fcurrency("setUpCost", "Set Up");
			cost[1] = fcurrency("monthlyCost", "Monthly");
			cost[2] = fcurrency("annualCost", "Annual");

			price = new TextField[3];
			price[0] = fcurrency("baseSetupPrice", "Set Up");
			price[1] = fcurrency("baseMonthlyPrice", "Monthly");
			price[2] = fcurrency("baseAnnualPrice", "Annual");

			addField(name);
			addField(code);
			addField(description);
			addFields(cost);
			addFields(price);
		}

		@Override
		protected Widget draw() {
			FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

			// first row
			canvas.addField(name);
			canvas.addField(code);
			canvas.addField(description);

			// pricing
			canvas.newRow();
			Grid g = new Grid(2, 3);
			g.setWidget(0, 0, cost[0]);
			g.setWidget(0, 1, cost[1]);
			g.setWidget(0, 2, cost[2]);
			g.setWidget(1, 0, price[0]);
			g.setWidget(1, 1, price[1]);
			g.setWidget(1, 2, price[2]);
			canvas.addWidget(g);

			// params
			params = new Grid(1, 3);
			params.setWidget(0, 0, new Label("Name"));
			params.setWidget(0, 1, new Label("Code"));
			params.setWidget(0, 3, new Label("Desc"));
			canvas.newRow();
			canvas.addWidget(params);

			return canvas.getCanvasWidget();
		}

	}

	/**
	 * Constructor
	 */
	public MultiOptionInterfacePanel() {
		super();
		tabOptions.addTabListener(this);
	}

	@Override
	public void populateFieldGroup() {
		super.populateFieldGroup();

		/*
		// clear existing options
		for(OptionPanel p : optionPanels) {
			fields.removeField(p.getFields());
		}
		optionPanels.clear();
		tabOptions.clear();

		// bind options
		RelatedManyProperty pvOptions = modelInterface.relatedMany("options");
		if(pvOptions != null && pvOptions.size() > 0) {
			for(IndexedProperty propOption : pvOptions) {
				Model option = propOption.getModel();
				OptionPanel pnlOption = new OptionPanel(propOption.getPropertyName());
				fields.addField(pnlOption.getFields());
				optionPanels.add(pnlOption);
				pnlOption.configure();
				pnlOption.onBeforeBind(option);
				tabOptions.add(pnlOption, option.getName());
			}
		}

		// add new option tab
		Image img = App.imgs().add().createImage();
		img.setTitle("Add...");
		tabOptions.add(new Label("TODO"), img);

		// default select the first tab
		tabOptions.selectTab(0);
		*/
	}

	@Override
	protected Widget draw() {
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);
		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(timestamps[0]);
		canvas.stopFlow();
		canvas.addField(timestamps[1]);
		canvas.resetAlignment();

		// availability
		canvas.newRow();
		canvas.addWidget(createAvailabilityGrid());

		// options tab widget
		canvas.newRow();
		canvas.addWidget(tabOptions);
		return canvas.getCanvasWidget();
	}

	@Override
	protected void applyModel(Model model) {

	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		/*
		assert sender == tabOptions;
		if(tabIndex == tabOptions.getTabBar().getTabCount() - 1) {
			OptionPanel op = new OptionPanel(FieldGroup.getPendingPropertyName());

			Model proto = AuxDataCache.instance().getEntityPrototype(EntityType.INTERFACE_OPTION);
			assert proto != null;
			op.bind(proto);
			fields.addField(op.getFields());

			tabOptions.insert(op, "New Option", tabIndex);
			// op.getFields().render();
			// tabOptions.remove(tabIndex + 1);
			// tabOptions.selectTab(tabIndex);
		}
		*/
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
	}

}
