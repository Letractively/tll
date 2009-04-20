package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.AbstractDynamicViewInitializer;
import com.tll.common.model.ModelKey;

/**
 * CustomerListingViewInitializer - CustomerListingView specific view request.
 * @author jpk
 */
public final class CustomerListingViewInitializer extends AbstractDynamicViewInitializer {

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
	CustomerListingViewInitializer(ModelKey mercRef, ModelKey merchantParentRef) {
		super(CustomerListingView.klas);
		this.mercRef = mercRef;
		this.merchantParentRef = merchantParentRef;
	}

	@Override
	protected int getViewId() {
		return mercRef.hashCode();
	}

	public ModelKey getMerchantParentRef() {
		return merchantParentRef;
	}

	public ModelKey getMercRef() {
		return mercRef;
	}

}