package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.IViewKey;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewKey;
import com.tll.common.model.ModelKey;

/**
 * CustomerListingViewRequest - CustomerListingView specific view request.
 * @author jpk
 */
public final class CustomerListingViewRequest extends ShowViewRequest {

	final ModelKey merchantParentRef;

	/**
	 * The parent Merchant ref.
	 */
	final ModelKey mercRef;

	/**
	 * Constructor
	 * @param mercRef The parent merchant ref
	 * @param merchantParentRef
	 */
	CustomerListingViewRequest(ModelKey mercRef, ModelKey merchantParentRef) {
		this.mercRef = mercRef;
		this.merchantParentRef = merchantParentRef;
	}

	@Override
	public IViewKey getViewKey() {
		return new ViewKey(CustomerListingView.klas, mercRef.hashCode());
	}

	public ModelKey getMerchantParentRef() {
		return merchantParentRef;
	}

	public ModelKey getMercRef() {
		return mercRef;
	}

}