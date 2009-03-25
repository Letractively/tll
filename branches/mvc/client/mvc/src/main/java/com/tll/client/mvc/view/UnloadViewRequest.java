/**
 * The Logic Lab
 * @author jpk Jan 17, 2008
 */
package com.tll.client.mvc.view;


/**
 * UnloadViewRequest
 * @author jpk
 */
public final class UnloadViewRequest extends ViewOpRequest {

	private final boolean removeFromCache;

	/**
	 * Constructor
	 * @param viewKey
	 * @param removeFromCache Remove the view from the view cache?
	 */
	public UnloadViewRequest(IViewKey viewKey, boolean removeFromCache) {
		super(viewKey);
		this.removeFromCache = removeFromCache;
	}

	@Override
	public boolean addHistory() {
		return false;
	}

	/**
	 * Remove the view from view cache? Subsequent view requests will have to
	 * completely re-load the view.
	 * @return true/false
	 */
	public boolean isRemoveFromCache() {
		return removeFromCache;
	}

}