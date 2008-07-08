/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.ui.field.DeleteTabWidget;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.listing.FieldListing;

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
		FieldListing paramListing;

		/**
		 * Constructor
		 * @param paramListing May be <code>null</code>
		 */
		public OptionPanel(FieldListing paramListing) {
			super("Option");
			this.paramListing = paramListing;
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
		protected void draw(Panel canvas) {
			final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
			cmpsr.setCanvas(canvas);

			// first row
			cmpsr.addField(name);
			cmpsr.addField(code);
			cmpsr.addField(description);

			// pricing
			cmpsr.newRow();
			Grid g = new Grid(2, 3);
			g.setWidget(0, 0, cost[0]);
			g.setWidget(0, 1, cost[1]);
			g.setWidget(0, 2, cost[2]);
			g.setWidget(1, 0, price[0]);
			g.setWidget(1, 1, price[1]);
			g.setWidget(1, 2, price[2]);
			cmpsr.addWidget(g);

			cmpsr.newRow();
			cmpsr.addWidget(paramListing);
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
	}

	@Override
	protected void draw(Panel canvas) {
		FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		// first row
		cmpsr.addField(name);
		cmpsr.addField(code);
		cmpsr.addField(description);
		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cmpsr.addField(timestamps[0]);
		cmpsr.stopFlow();
		cmpsr.addField(timestamps[1]);
		cmpsr.resetAlignment();

		// availability
		cmpsr.newRow();
		cmpsr.addWidget(createAvailabilityGrid());

		// options tab widget
		cmpsr.newRow();
		cmpsr.addWidget(tabOptions);
	}

	@Override
	protected void applyModel(Model model) {

		// clear existing options
		for(Widget w : tabOptions) {
			if(w instanceof OptionPanel) removeField(((OptionPanel) w).getFields());
		}
		tabOptions.clear();

		final PropertyPath path = new PropertyPath();

		// bind options
		path.parse("options");
		RelatedManyProperty pvOptions = model.relatedMany(path);
		if(pvOptions != null && pvOptions.size() > 0) {
			for(IndexedProperty propOption : pvOptions) {
				Model option = propOption.getModel();

				// params
				path.parse(propOption.getPropertyName());
				path.append("parameters");
				RelatedManyProperty pvParams = model.relatedMany(path);
				if(pvParams != null && pvParams.size() > 0) {

					for(IndexedProperty propParam : pvParams) {
						path.parse(propOption.getPropertyName());
						path.append(propParam.getPropertyName());
						// param specific fields:
						addField(path.toString(), createNameEntityField());
						addField(path.toString(), ftext("code", "Code", 20));
						addField(path.toString(), ftextarea("description", "Desc", 3, 20));
					}

				}

				OptionPanel pnlOption =
						new OptionPanel(new FieldListing("Parameters", paramColumns, pvParams.getPropertyName(), getFields(),
								new ParamFieldRenderer(getFields(), path.toString())));
				addField(propOption.getPropertyName(), pnlOption.getFields());
				tabOptions.add(pnlOption, new DeleteTabWidget(option.getName(), pnlOption.getFields(), propOption
						.getPropertyName()));
			}
		}

		// add new option tab
		Image img = App.imgs().add().createImage();
		img.setTitle("Add...");
		tabOptions.add(new Label("TODO"), img);
	}

	@Override
	public void onAfterBind() {
		super.onAfterBind();
		// default select the first tab if none are selected
		if(tabOptions.getTabBar().getSelectedTab() < 0) tabOptions.selectTab(0);
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		OptionPanel op = (OptionPanel) tabOptions.getWidget(tabIndex);
		op.paramListing.refresh();
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
	}

}
