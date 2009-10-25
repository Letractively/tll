package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.AbstractDynamicViewInitializer;
import com.tll.common.model.ModelKey;

/**
 * CustomerListingViewInitializer - CustomerListingView specific view request.
 * @author jpk
 */
public final class CustomerListingViewInitializer extends AbstractDynamicViewInitializer {

	/**
	 * The parent account's parent account ref.
	 */
	private final ModelKey grandParentAccountRef;

	/**
	 * The parent account ref.
	 */
	private final ModelKey parentAccountRef;

	/**
	 * Constructor
	 * @param parentAccountRef The parent merchant ref
	 * @param grandParentAccountRef optional
	 */
	public CustomerListingViewInitializer(ModelKey parentAccountRef, ModelKey grandParentAccountRef) {
		super(CustomerListingView.klas);
		this.parentAccountRef = parentAccountRef;
		this.grandParentAccountRef = grandParentAccountRef;
	}

	@Override
	protected int getViewId() {
		return parentAccountRef.hashCode();
	}

	public ModelKey getParentAccountRef() {
		return parentAccountRef;
	}

	public ModelKey getGrandParentAccountRef() {
		return grandParentAccountRef;
	}
}