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
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.DeleteTabWidget;
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
		final ParameterListingPanel paramListing;

		/**
		 * Constructor
		 * @param paramListing May be <code>null</code>
		 */
		public OptionPanel(ParameterListingPanel paramListing) {
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

			canvas.newRow();
			canvas.addWidget(paramListing);

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
	public void applyModel(Model model) {

		// clear existing options
		for(Widget w : tabOptions) {
			removeField(((OptionPanel) w).getFields());
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
				final ParameterPanel[] paramPanels = new ParameterPanel[(pvParams == null ? 0 : pvParams.size())];
				if(pvParams != null && pvParams.size() > 0) {

					int i = 0;
					for(IndexedProperty propParam : pvParams) {
						// Model param = propParam.getModel();
						ParameterPanel pnlParam = new ParameterPanel();
						path.parse(propOption.getPropertyName());
						path.append(propParam.getPropertyName());
						addField(path.toString(), pnlParam.getFields());
						paramPanels[i++] = pnlParam;
					}

				}

				OptionPanel pnlOption = new OptionPanel(new ParameterListingPanel(paramPanels));
				addField(propOption.getPropertyName(), pnlOption.getFields());
				tabOptions.add(pnlOption, new DeleteTabWidget(option.getName(), pnlOption.getFields()));
			}
		}

		// add new option tab
		Image img = App.imgs().add().createImage();
		img.setTitle("Add...");
		tabOptions.add(new Label("TODO"), img);

		// default select the first tab
		tabOptions.selectTab(0);
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
	}

}
