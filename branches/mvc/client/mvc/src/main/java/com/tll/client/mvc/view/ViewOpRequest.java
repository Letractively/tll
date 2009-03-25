/**
 * The Logic Lab
 * @author jpk
 * Apr 5, 2008
 */
package com.tll.client.mvc.view;


/**
 * ViewOpRequest
 * @author jpk
 */
public abstract class ViewOpRequest extends AbstractViewRequest {

	private final IViewKey viewKey;

	/**
	 * Constructor
	 * @param viewKey
	 */
	public ViewOpRequest(IViewKey viewKey) {
		super();
		this.viewKey = viewKey;
	}

	@Override
	public IViewKey getViewKey() {
		return viewKey;
	}

}
