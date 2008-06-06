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
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.TextField;

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
	protected Widget doInit() {
		// super.doInit();

		final TextField fname = createNameEntityField();
		fields.addField(fname);

		final DateField[] ftimestamps = createTimestampEntityFields();
		fields.addFields(ftimestamps);

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(fname);
		canvas.addField(code);
		canvas.addField(description);

		canvas.addWidget(createAvailabilityGrid());

		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(ftimestamps[0]);
		canvas.stopFlow();
		canvas.addField(ftimestamps[1]);
		canvas.resetAlignment();

		canvas.newRow();
		canvas.addWidget(tabOptions);

		return canvas.getWidget();
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
