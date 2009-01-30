/**
 * The Logic Lab
 * @author jpk Jan 17, 2008
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * UnloadViewRequest
 * @author jpk
 */
public final class UnloadViewRequest extends ViewOpRequest {

	private final boolean removeFromCache;

	/**
	 * Constructor
	 * @param source
	 * @param viewKey
	 * @param removeFromCache Remove the view from the view cache?
	 */
	public UnloadViewRequest(Widget source, ViewKey viewKey, boolean removeFromCache) {
		super(source, viewKey);
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