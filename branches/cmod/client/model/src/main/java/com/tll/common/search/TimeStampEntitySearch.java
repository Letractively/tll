/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.criteria.CriteriaType;
import com.tll.model.IEntityType;
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
	public TimeStampEntitySearch(CriteriaType criteriaType, IEntityType entityType) {
		super(criteriaType, entityType);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 * @param entityType
	 */
	public TimeStampEntitySearch(String businessKeyName, IEntityType entityType) {
		super(businessKeyName, entityType);
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
