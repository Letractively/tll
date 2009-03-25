package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewKey;
import com.tll.common.model.ModelKey;

/**
 * MerchantListingViewRequest - MerchantListingView specific view request.
 * @author jpk
 */
public final class MerchantListingViewRequest extends ShowViewRequest {

	/**
	 * The parent Isp ref.
	 */
	final ModelKey ispRef;

	/**
	 * Constructor
	 * @param ispRef The required parent isp ref
	 */
	MerchantListingViewRequest(ModelKey ispRef) {
		this.ispRef = ispRef;
	}

	@Override
	public IViewKey getViewKey() {
		return new ViewKey(MerchantListingView.klas, ispRef.hashCode());
	}

	public ModelKey getIspRef() {
		return ispRef;
	}

}