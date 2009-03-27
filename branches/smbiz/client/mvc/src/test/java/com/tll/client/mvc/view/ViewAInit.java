/**
 * The Logic Lab
 * @author jpk
 * @since Mar 25, 2009
 */
package com.tll.client.mvc.view;


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
	protected int getViewId() {
		return "ViewA".hashCode();
	}
}
