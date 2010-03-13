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
	protected int getViewId() {
		return dyn.hashCode();
	}
}
