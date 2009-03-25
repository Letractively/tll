package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewKey;

/**
 * IspListingViewRequest
 * @author jpk
 */
public final class IspListingViewRequest extends ShowViewRequest {

	private final IViewKey viewKey;

	/**
	 * Constructor
	 */
	public IspListingViewRequest() {
		viewKey = new ViewKey(IspListingView.klas, 0);
	}

	@Override
	public IViewKey getViewKey() {
		return viewKey;
	}
}