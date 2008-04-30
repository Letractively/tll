/**
 * The Logic Lab
 * @author jpk
 * Apr 6, 2008
 */
package com.tll.client.listing;

/**
 * ClientListingOp - Listing ops for use on the client side.
 * @author jpk
 */
public enum ClientListingOp {
	REFRESH,
	GOTO_PAGE,
	FIRST_PAGE,
	LAST_PAGE,
	PREVIOUS_PAGE,
	NEXT_PAGE,
	SORT,
	CLEAR,
	INSERT_ROW,
	UPDATE_ROW,
	DELETE_ROW;
}