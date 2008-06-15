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
 * Represents the state of the table model w/o holding any actual row data.
 * @author jpk
 */
public class ListingState {

	/**
	 * The 0-based list index offset.
	 */
	private Integer offset;

	/**
	 * The sorting directive.
	 */
	private Sorting sorting;

	/**
	 * Constructor
	 */
	public ListingState() {
		super();
	}

	/**
	 * Constructor
	 * @param offset
	 * @param sorting
	 */
	public ListingState(Integer offset, Sorting sorting) {
		this();
		setOffset(offset);
		setSorting(sorting);
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer pageNumber) {
		this.offset = pageNumber;
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("offset", getOffset()).append("sorting",
				getSorting() == null ? null : getSorting().toString()).toString();
	}
}
