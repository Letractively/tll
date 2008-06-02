/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.ui.FlowFieldPanelComposer;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 */
public final class MultiOptionInterfacePanel extends AbstractInterfacePanel implements TabListener {

	private final TabPanel tabOptions = new TabPanel();

	/**
	 * Constructor
	 */
	public MultiOptionInterfacePanel() {
		super();
		tabOptions.addTabListener(this);
	}

	@Override
	protected void doInit() {
		super.doInit();

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);

		canvas.addWidget(createAvailabilityGrid());

		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(dateCreated);
		canvas.stopFlow();
		canvas.addField(dateModified);
		canvas.resetAlignment();

		canvas.newRow();
		canvas.addWidget(tabOptions);
	}

	/*
	@Override
	protected void onBeforeBind(Model modelInterface) {
		super.onBeforeBind(modelInterface);

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
	}
	*/

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
