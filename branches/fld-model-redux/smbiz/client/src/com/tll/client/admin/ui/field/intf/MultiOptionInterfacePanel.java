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
import com.tll.client.field.FieldGroup;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.ui.field.DeleteTabWidget;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;
import com.tll.model.EntityType;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 */
public final class MultiOptionInterfacePanel extends AbstractInterfacePanel implements TabListener {

	private final TabPanel tabOptions = new TabPanel();

	private static final class OptionPanel extends FieldPanel {

		TextField name, code;
		TextAreaField description;
		TextField[] cost, price;

		// FieldListing paramListing;

		/**
		 * Constructor
		 */
		public OptionPanel() {
			super("Option");
		}

		@Override
		protected void populateFieldGroup(FieldGroup fields) {
			name = entityNameField();
			code = ftext("code", "Code", 20, null);
			description = ftextarea("description", "Desc", 3, 8, null);

			cost = new TextField[3];
			cost[0] = fcurrency("setUpCost", "Set Up", null);
			cost[1] = fcurrency("monthlyCost", "Monthly", null);
			cost[2] = fcurrency("annualCost", "Annual", null);

			price = new TextField[3];
			price[0] = fcurrency("baseSetupPrice", "Set Up", null);
			price[1] = fcurrency("baseMonthlyPrice", "Monthly", null);
			price[2] = fcurrency("baseAnnualPrice", "Annual", null);

			fields.addField(name);
			fields.addField(code);
			fields.addField(description);
			fields.addFields(cost);
			fields.addFields(price);
		}

		@Override
		protected void drawInternal(Panel canvas) {
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

			// cmpsr.newRow();
			// cmpsr.addWidget(paramListing);
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
	public void populateFieldGroup(FieldGroup fields) {
		super.populateFieldGroup(fields);
	}

	@Override
	protected void drawInternal(Panel canvas) {
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

	private void rebuildOptions(IFieldGroupModelBinding bindingDef) {
		final FieldGroup fields = getFieldGroup();
		final Model model = bindingDef.resolveModel(EntityType.ACCOUNT);
		assert model != null && fields != null;

		// clear existing options
		for(Widget w : tabOptions) {
			if(w instanceof OptionPanel) fields.removeField(((OptionPanel) w).getFieldGroup());
		}
		tabOptions.clear();

		// bind options
		RelatedManyProperty pvOptions;
		try {
			pvOptions = model.relatedMany("options");
		}
		catch(PropertyPathException e) {
			throw new IllegalStateException();
		}
		if(pvOptions != null && pvOptions.size() > 0) {
			for(IndexedProperty propOption : pvOptions) {
				Model option = propOption.getModel();

				// params
				// TODO re-impl!
				/*
				RelatedManyProperty pvParams =
						model.relatedMany(PropertyPath.getPropertyPath(propOption.getPropertyName(), "parameters"));
				if(pvParams != null && pvParams.size() > 0) {
					PropertyPath paramPath = new PropertyPath();
					for(IndexedProperty propParam : pvParams) {
						paramPath.parse(propOption.getPropertyName());
						paramPath.append(propParam.getPropertyName());
						// param specific fields:
						addFields(paramPath.toString(), paramFieldProvider.getFields());
					}

				}
				*/

				OptionPanel pnlOption = new OptionPanel();
				tabOptions.add(pnlOption, new DeleteTabWidget(option.getName(), pnlOption.getFieldGroup(), bindingDef,
						propOption.getPropertyName()));
			}
		}

		// add new option tab
		Image img = App.imgs().add().createImage();
		img.setTitle("Add...");
		tabOptions.add(new Label("TODO"), img);
	}

	/*
	@Override
	public void onAfterBind() {
		super.onAfterBind();
		// default select the first tab if none are selected
		if(tabOptions.getTabBar().getSelectedTab() < 0) tabOptions.selectTab(0);
	}
	*/

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		// TODO re-impl
		// OptionPanel op = (OptionPanel) tabOptions.getWidget(tabIndex);
		// op.paramListing.refresh();
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
	}

}
