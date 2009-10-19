/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.ui.listing.MerchantListingWidget;
import com.tll.client.ui.view.ViewLink;
import com.tll.common.model.ModelKey;

/**
 * MerchantListingView
 * @author jpk
 */
public final class MerchantListingView extends ListingView<MerchantListingViewInitializer, MerchantListingWidget> {

	public static final Class klas = new Class();

	public static final class Class extends AbstractListingViewClass {

		@Override
		public String getName() {
			return "MerchantListingView";
		}

		@Override
		public MerchantListingView newView() {
			return new MerchantListingView();
		}
	}

	/**
	 * Ref of the parent ISP
	 */
	private ModelKey ispRef;

	/**
	 * The link to the parent isp listing view.
	 */
	private final ViewLink ispListingLink = new ViewLink();

	/**
	 * Constructor
	 */
	public MerchantListingView() {
		super();
		addWidget(ispListingLink);
	}

	@Override
	public void doInitialization(MerchantListingViewInitializer r) {
		assert r.ispRef != null && r.ispRef.isSet();
		ispRef = r.ispRef;

		ispListingLink.setViewInitializer(new StaticViewInitializer(IspListingView.klas));
		ispListingLink.setText(ispRef.getName());

		setListingWidget(new MerchantListingWidget(r.ispRef));
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}

	@Override
	public String getShortViewName() {
		return "Merchant Listing";
	}

	@Override
	public String getLongViewName() {
		assert ispRef != null;
		return "Merchant Listing for " + ispRef.descriptor();
	}
}
