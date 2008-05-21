/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field;

import com.tll.client.model.Model;

/**
 * InterfaceSinglePanel
 * @author jpk
 */
public final class InterfaceSwitchPanel extends InterfacePanel {

	/**
	 * Constructor
	 * @param propName
	 */
	public InterfaceSwitchPanel(String propName) {
		super(propName);
	}

	@Override
	protected void onBeforeBind(Model modelInterface) {
		super.onBeforeBind(modelInterface);
	}

	@Override
	protected void uiAddOption(InterfaceOptionPanel optionPanel, Model option) {
		add(optionPanel);
	}

	@Override
	protected void uiOptionsClear() {
		// no-op
	}
}
