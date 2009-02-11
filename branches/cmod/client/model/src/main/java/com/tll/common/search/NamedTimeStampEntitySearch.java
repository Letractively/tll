/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.common.model.IEntityType;
import com.tll.criteria.CriteriaType;

/**
 * TimeStampEntitySearch
 * @author jpk
 */
public abstract class NamedTimeStampEntitySearch extends TimeStampEntitySearch {

	protected String name;

	/**
	 * Constructor
	 */
	public NamedTimeStampEntitySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param criteriaType
	 * @param entityType
	 */
	public NamedTimeStampEntitySearch(CriteriaType criteriaType, IEntityType entityType) {
		super(criteriaType, entityType);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 * @param entityType
	 */
	public NamedTimeStampEntitySearch(String businessKeyName, IEntityType entityType) {
		super(businessKeyName, entityType);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void clear() {
		super.clear();
		name = null;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " name:" + name;
		return s;
	}
}
