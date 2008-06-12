/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.search.ISearch;
import com.tll.client.ui.listing.AbstractListingWidget;

/**
 * ListingFactory - Assembles listing Widgets used for showing listing data.
 * @author jpk
 */
public abstract class ListingFactory {

	/*
		if(rowOptionsDelegate != null) {
			rowContextPopup = new RowContextPopup(rowOptionsDelegate);
			table.addTableListener(rowContextPopup);
			getListingPanel().addMouseListener(rowContextPopup);
		}
		else {
			rowContextPopup = null;
		}

		navBar.setAddRowDelegate(addRowDelegate);
	 */

	/**
	 * Factory method for a listing Widget.
	 * @param config The listing config
	 * @param rowOptionsDelegate May be <code>null</code>
	 * @param addRowDelegate May be <code>null</code>
	 * @return A new listing Widget
	 */
	private static AbstractListingWidget assembleListingWidget(IListingConfig config,
			IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		return new RowContextListingWidget(config, rowOptionsDelegate, addRowDelegate);
	}

	/**
	 * Assembles a listing Widget.
	 * @param <S> The search type
	 * @param config The listing config
	 * @param operator The listing operator
	 * @param rowOptionsDelegate Optional. If <code>null</code>, no row options
	 *        will be available.
	 * @param addRowDelegate The optional add row delegate that will handle adding
	 *        rows.
	 * @return A new listing Widget
	 */
	public static <S extends ISearch> AbstractListingWidget create(IListingConfig config, IListingOperator operator,
			IRowOptionsDelegate rowOptionsDelegate, IAddRowDelegate addRowDelegate) {
		AbstractListingWidget listingWidget = assembleListingWidget(config, rowOptionsDelegate, addRowDelegate);
		operator.setListingWidget(listingWidget);
		return listingWidget;
	}
}
