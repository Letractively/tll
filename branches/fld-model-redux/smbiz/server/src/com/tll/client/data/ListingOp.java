/*
 * Created on - Nov 10, 2005 Coded by - 'The Logic Lab' - jpk Copywright - 2005 -
 * All rights reserved.
 */

package com.tll.client.data;

import com.tll.util.INameValueProvider;

/**
 * ListingOp - Defines the possible listing operations on a server-side listing.
 * @author jpk
 */
public enum ListingOp implements INameValueProvider {

	/**
	 * Generates or refreshes a listing clearing any existing cache.
	 */
	REFRESH("Refresh"),

	/**
	 * Fetch page data on an existing listing.
	 */
	FETCH("Fetch"),

	/**
	 * Clears any cached listing data for a particular listing.
	 */
	CLEAR("Clear"),

	/**
	 * Clears all cached listing data for <em>all</em> cached listings.
	 */
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

	public boolean isQuery() {
		return this == ListingOp.REFRESH || this == ListingOp.FETCH;
	}

	public boolean isClear() {
		return this == ListingOp.CLEAR || this == ListingOp.CLEAR_ALL;
	}
}
