/**
 * 
 */
package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.ListingView;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.listing.IspListingWidget;

/**
 * IspListingView
 * @author jpk
 */
public final class IspListingView extends ListingView<StaticViewInitializer, IspListingWidget> {

	public static final Class klas = new Class();

	public static final class Class extends AbstractListingViewClass {

		@Override
		public String getName() {
			return "IspListingView";
		}

		@Override
		public IspListingView newView() {
			return new IspListingView();
		}
	}

	/**
	 * Constructor
	 */
	public IspListingView() {
		super();
	}

	@Override
	public ViewClass getViewClass() {
		return klas;
	}

	@Override
	protected void doInitialization(StaticViewInitializer init) {
		setListingWidget(new IspListingWidget());
	}

	@Override
	public String getLongViewName() {
		return "Isp Listing";
	}
}
