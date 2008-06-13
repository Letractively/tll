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
     * The 0-based page number.
     */
    private Integer   pageNumber;
    private Sorting   sorting;
    
    public ListingState() {
        super();
    }
    public ListingState(Integer pageNumber, Sorting sorting) {
        this();
        setPageNumber(pageNumber);
        setSorting(sorting);
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

    @Override
    public String toString() {
		return new ToStringBuilder(this)
			.append("pageNumber", getPageNumber())
			.append("sorting", getSorting()==null? null : getSorting().toString())
		.toString();
    }
}
