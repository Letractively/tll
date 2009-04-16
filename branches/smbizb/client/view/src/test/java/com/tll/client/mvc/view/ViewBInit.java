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
public class ViewBInit extends AbstractDynamicViewInitializer {

	/**
	 * Constructor
	 */
	public ViewBInit() {
		super(ViewB.klas);
	}

	@Override
	protected int getViewId() {
		return "ViewB".hashCode();
	}
}
