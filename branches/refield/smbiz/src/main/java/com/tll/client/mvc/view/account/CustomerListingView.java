/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.ListingView;
import com.tll.client.ui.listing.CustomerListingWidget;
import com.tll.client.ui.view.ViewLink;

/**
 * CustomerListingView
 * @author jpk
 */
public final class CustomerListingView extends ListingView<CustomerListingViewInitializer, CustomerListingWidget> {

	public static final Class klas = new Class();

	public static final class Class extends AbstractListingViewClass {

		@Override
		public String getName() {
			return "CustomerListingView";
		}

		@Override
		public CustomerListingView newView() {
			return new CustomerListingView();
		}
	}

	/**
	 * The link to the parent listing view.
	 */
	private final ViewLink parentLink = new ViewLink();

	/**
	 * Constructor
	 */
	public CustomerListingView() {
		super();
		addWidget(parentLink);
	}

	@Override
	public void doInitialization(CustomerListingViewInitializer r) {
		assert r.mercRef != null && r.mercRef.isSet();

		if(r.getMerchantParentRef() != null) {
			parentLink.setViewInitializer(new MerchantListingViewInitializer(r.getMerchantParentRef()));
			parentLink.setText(r.getMerchantParentRef().getName());
		}
		parentLink.setVisible(r.getMerchantParentRef() != null);

		setListingWidget(new CustomerListingWidget(r.mercRef));
	}

	@Override
	public String getShortViewName() {
		return "Customer Listing";
	}

	@Override
	public String getLongViewName() {
		assert listingWidget.getParentAccountRef() != null;
		return "Customer Listing for " + listingWidget.getParentAccountRef().descriptor();
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}
}
