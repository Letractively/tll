/*
 * Created on - Nov 10, 2005 Coded by - 'The Logic Lab' - jpk Copywright - 2005 -
 * All rights reserved.
 */

package com.tll.client.data;

import com.tll.util.INameValueProvider;

/**
 * ListingOp - Encapsulates all available actions on a server side listing.
 * @author jpk
 */
public enum ListingOp implements INameValueProvider {

	/**
	 * [Re-]Generate the listing clearing any cache.
	 */
	REFRESH("Refresh"),

	/**
	 * Retrieve cached listing data or generate the listing if not cached.
	 */
	DISPLAY("Display"),

	GOTO_PAGE("Goto page"),

	FIRST_PAGE("First page"),

	LAST_PAGE("Last page"),

	PREVIOUS_PAGE("Previous page"),

	NEXT_PAGE("Next page"),

	SORT("Sort"),

	CLEAR("Clear"),

	CLEAR_ALL("Clear all");

	private final String name;

	/**
	 * Constructor
	 * @param name
	 */
	private ListingOp(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

	public boolean isClear() {
		return this == ListingOp.CLEAR || this == ListingOp.CLEAR_ALL;
	}

	public boolean isNavigation() {
		return this == ListingOp.FIRST_PAGE || this == ListingOp.LAST_PAGE || this == ListingOp.PREVIOUS_PAGE
				|| this == ListingOp.NEXT_PAGE || this == ListingOp.GOTO_PAGE;
	}
}
