/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

/**
 * InterfaceSinglePanel - <em>One</em> or more options are allowed where only
 * one option may be "selected" (the default) at any one time.
 * @author jpk
 */
public final class InterfaceSinglePanel extends InterfaceMultiOptionPanel {

	/**
	 * Constructor
	 * @param propName
	 */
	public InterfaceSinglePanel(String propName) {
		super(propName);
	}
}
