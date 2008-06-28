/*
 * Created on - Mar 18, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 *
 */

package com.tll.server.rpc.listing;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.tll.listhandler.Sorting;

/**
 * ListingState - Holds bare bones listing state for a single listing.
 * @author jpk
 */
public final class ListingState {

	/**
	 * The 0-based list index offset.
	 */
	private final Integer offset;

	/**
	 * The sorting directive.
	 */
	private final Sorting sorting;

	/**
	 * Constructor
	 * @param offset
	 * @param sorting
	 * @throws IllegalArgumentException When any of the arguments are
	 *         <code>null</code>
	 */
	public ListingState(Integer offset, Sorting sorting) throws IllegalArgumentException {
		super();
		if(offset == null) {
			throw new IllegalArgumentException("An offset must be specified");
		}
		if(sorting == null) {
			throw new IllegalArgumentException("A sorting directive must be specified");
		}
		this.offset = offset;
		this.sorting = sorting;
	}

	public Integer getOffset() {
		return offset;
	}

	public Sorting getSorting() {
		return sorting;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("offset", getOffset()).append("sorting",
				getSorting() == null ? null : getSorting().toString()).toString();
	}
}
