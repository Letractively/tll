/**
 * The Logic Lab
 * @author jpk
 * @since Mar 25, 2009
 */
package com.tll.client.mvc.view;

import com.tll.common.model.ModelKey;


/**
 * ViewAInit
 * @author jpk
 */
public class ViewBInit extends AbstractDynamicViewInitializer {

	private final ModelKey mk;

	/**
	 * Constructor
	 * @param mk
	 */
	public ViewBInit(ModelKey mk) {
		super(ViewB.klas);
		this.mk = mk;
	}

	@Override
	protected int getViewId() {
		return mk.hashCode();
	}
}
