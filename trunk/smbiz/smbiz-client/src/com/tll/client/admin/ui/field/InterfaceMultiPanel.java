/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.tll.client.App;
import com.tll.client.model.Model;

/**
 * InterfaceMultiPanel
 * @author jpk
 */
public final class InterfaceMultiPanel extends InterfacePanel implements TabListener {

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

		// add new option tab
		Image img = App.imgs().add().createImage();
		img.setTitle("Add...");
		tabOptions.add(new Label("TODO"), img);

		// default select the first tab
		tabOptions.selectTab(0);
	}

	@Override
	protected void uiAddOption(InterfaceOptionPanel optionPanel, Model option) {
		tabOptions.add(optionPanel, option.getName());
	}

	@Override
	protected void uiOptionsClear() {
		tabOptions.clear();
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
