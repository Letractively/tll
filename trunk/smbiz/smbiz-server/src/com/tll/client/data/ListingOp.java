/*
 * Created on - Nov 10, 2005 Coded by - 'The Logic Lab' - jpk Copywright - 2005 -
 * All rights reserved.
 */

package com.tll.client.data;

import com.tll.client.IMarshalable;
import com.tll.listhandler.Sorting;

/**
 * ListingOp - Encapsulates all available actions on a server side listing.
 * @author jpk
 */
public final class ListingOp implements IMarshalable {

	/**
	 * [Re-]Generate the listing clearing any cache.
	 */
	public static final int REFRESH = 0;

	/**
	 * Retrieve cached listing data or generate the listing if not cached.
	 */
	public static final int DISPLAY = 1;

	public static final int GOTO_PAGE = 2;
	public static final int FIRST_PAGE = 3;
	public static final int LAST_PAGE = 4;
	public static final int PREVIOUS_PAGE = 5;
	public static final int NEXT_PAGE = 6;
	public static final int SORT = 7;
	public static final int CLEAR = 8;
	public static final int CLEAR_ALL = 9;

	private int op = -1; // undefined

	/**
	 * The 1-based page number.
	 */
	private Integer pageNumber = null;

	private Sorting sorting;

	private Boolean retainStateOnClear = Boolean.TRUE;

	/**
	 * Constructor
	 */
	public ListingOp() {
		super();
	}

	/**
	 * Constructor
	 * @param op
	 */
	public ListingOp(int op) {
		this();
		this.op = op;
	}

	public String descriptor() {
		switch(op) {
			case REFRESH:
				return "Refresh";
			case DISPLAY:
				return "Display";
			case GOTO_PAGE:
				return "Goto page" + (pageNumber == null ? "" : (" " + pageNumber.toString()));
			case FIRST_PAGE:
				return "Goto First Page";
			case LAST_PAGE:
				return "Goto Last Page";
			case PREVIOUS_PAGE:
				return "Previous Page";
			case NEXT_PAGE:
				return "Next Page";
			case SORT:
				return "Sort";
			case CLEAR:
				return "Clear";
			case CLEAR_ALL:
				return "Clear All";
			default:
				return "UNKNOWN (" + op + ")";
		}
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	public Boolean getRetainStateOnClear() {
		return retainStateOnClear;
	}

	public void setRetainStateOnClear(Boolean retainStateOnClear) {
		this.retainStateOnClear = retainStateOnClear;
	}

	@Override
	public String toString() {
		return descriptor() + ", page num:" + getPageNumber() + ", sorting:"
				+ (getSorting() == null ? "null" : getSorting().toString()) + ", retainStateOnClear:" + retainStateOnClear;
	}
}
