/**
 * The Logic Lab
 * @author jpk
 * @since Mar 25, 2009
 */
package com.tll.client.view;

import com.tll.client.view.AbstractDynamicViewInitializer;


/**
 * ViewAInit
 * @author jpk
 */
public class ViewAInit extends AbstractDynamicViewInitializer {

	/**
	 * Constructor
	 */
	public ViewAInit() {
		super(ViewA.klas);
	}

	@Override
	protected String getInstanceToken() {
		return "ViewA";
	}
}
