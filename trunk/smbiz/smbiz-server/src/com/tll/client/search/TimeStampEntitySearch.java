/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.client.search;

import com.tll.criteria.CriteriaType;
import com.tll.model.EntityType;
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
	 * @param criteriaType
	 * @param entityType
	 */
	public TimeStampEntitySearch(CriteriaType criteriaType, EntityType entityType) {
		super(criteriaType, entityType);
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
	public String toString() {
		String s = super.toString();
		s += " dcr:" + dcr;
		s += " dmr:" + dmr;
		return s;
	}
}
