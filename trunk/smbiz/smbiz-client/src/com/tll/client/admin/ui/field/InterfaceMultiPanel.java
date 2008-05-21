/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.App;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;

/**
 * InterfaceMultiPanel
 * @author jpk
 */
public final class InterfaceMultiPanel extends InterfacePanel implements TabListener {

	protected final List<InterfaceOptionPanel> optionPanels = new ArrayList<InterfaceOptionPanel>();

	private final TabPanel tabOptions = new TabPanel();

	/**
	 * Constructor
	 * @param propName
	 */
	public InterfaceMultiPanel(String propName) {
		super(propName);
		tabOptions.addTabListener(this);
	}

	@Override
	protected void configure() {
		super.configure();
		add(tabOptions);
	}

	@Override
	protected void onBeforeBind(Model modelInterface) {
		super.onBeforeBind(modelInterface);

		// clear existing options
		for(InterfaceOptionPanel p : optionPanels) {
			fields.removeField(p.getFields());
		}
		optionPanels.clear();
		tabOptions.clear();

		// bind options
		RelatedManyProperty pvOptions = modelInterface.relatedMany("options");
		if(pvOptions != null && pvOptions.size() > 0) {
			for(IndexedProperty propOption : pvOptions) {
				Model option = propOption.getModel();
				InterfaceOptionPanel pnlOption = new InterfaceOptionPanel(propOption.getPropertyName());
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

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		assert sender == tabOptions;
		if(tabIndex == tabOptions.getTabBar().getTabCount() - 1) {
			// we are adding
		}
		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
	}

}
