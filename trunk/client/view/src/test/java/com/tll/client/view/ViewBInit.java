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
public class ViewBInit extends AbstractDynamicViewInitializer {

	private final String dyn;

	/**
	 * Constructor
	 * @param dyn
	 */
	public ViewBInit(String dyn) {
		super(ViewB.klas);
		this.dyn = dyn;
	}

	@Override
	protected String getInstanceToken() {
		return dyn;
	}
}
