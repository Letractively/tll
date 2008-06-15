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
	REFRESH("Refresh", true),

	/**
	 * Fetches listing data.
	 */
	FETCH("Fetch", false),

	/**
	 * Clears any cached listing data for a particular listing.
	 */
	CLEAR("Clear", true),

	/**
	 * Clears all listing data for <em>all</em> cached listings.
	 */
	CLEAR_ALL("Clear all", true);

	private final String name;

	/**
	 * Force the operation?
	 */
	private final boolean force;

	/**
	 * Constructor
	 * @param name
	 */
	private ListingOp(String name, boolean force) {
		this.name = name;
		this.force = force;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

	public boolean isForce() {
		return force;
	}

	public boolean isClear() {
		return this == ListingOp.CLEAR || this == ListingOp.CLEAR_ALL;
	}
}
