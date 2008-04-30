/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.client.search;

import com.tll.util.DateRange;

/**
 * TimeStampEntitySearch
 * @author jpk
 */
public abstract class TimeStampEntitySearch extends EntitySearch {

	/**
	 * date created date range
	 */
	protected DateRange dcr;
	/**
	 * date modified date range
	 */
	protected DateRange dmr;

	/**
	 * Constructor
	 */
	public TimeStampEntitySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param searchType
	 * @param entityType
	 */
	public TimeStampEntitySearch(int searchType, String entityType) {
		super(searchType, entityType);
	}

	public final DateRange getDateCreatedRange() {
		return dcr;
	}

	public final void setDateCreatedRange(DateRange dcr) {
		this.dcr = dcr;
	}

	public final DateRange getDateModifiedRange() {
		return dmr;
	}

	public final void setDateModifiedRange(DateRange dmr) {
		this.dmr = dmr;
	}

	@Override
	public void clear() {
		super.clear();

		if(dcr != null) dcr.clear();
		if(dmr != null) dmr.clear();
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj) || obj instanceof TimeStampEntitySearch == false) return false;
		final TimeStampEntitySearch that = (TimeStampEntitySearch) obj;
		if(that.dcr == null) {
			if(dcr != null) return false;
		}
		else {
			if(dcr == null || !dcr.equals(that.dcr)) return false;

		}
		if(that.dmr == null) {
			if(dmr != null) return false;
		}
		else {
			if(dmr == null || !dmr.equals(that.dmr)) return false;

		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		if(dcr != null) {
			hash = hash * 31 + dcr.hashCode();
		}
		if(dmr != null) {
			hash = hash * 37 + dmr.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " dcr:" + dcr;
		s += " dmr:" + dmr;
		return s;
	}
}
