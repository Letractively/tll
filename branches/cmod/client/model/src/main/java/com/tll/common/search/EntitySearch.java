/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.criteria.CriteriaType;
import com.tll.model.IEntityType;

/**
 * EntitySearch
 * @author jpk
 */
public abstract class EntitySearch extends SearchBase {

	/**
	 * Corres. to the string-wise representation of the IEntityType enum.
	 */
	protected IEntityType entityType;

	/**
	 * The name of the server side business key
	 */
	private String businessKeyName;

	/**
	 * Constructor
	 */
	public EntitySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param criteriaType
	 * @param entityType
	 */
	public EntitySearch(CriteriaType criteriaType, IEntityType entityType) {
		super(criteriaType);
		this.entityType = entityType;
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 * @param entityType
	 */
	public EntitySearch(String businessKeyName, IEntityType entityType) {
		super();
		this.entityType = entityType;
		this.businessKeyName = businessKeyName;
	}

	public final IEntityType getEntityType() {
		return entityType;
	}

	public final void setEntityType(IEntityType entityType) {
		this.entityType = entityType;
	}

	@Override
	public void clear() {
		super.clear();
		// no-op
	}

	public final String getBusinessKeyName() {
		return businessKeyName;
	}

	public final void setBusinessKeyName(String businessKeyName) {
		this.businessKeyName = businessKeyName;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " entityType:" + entityType;
		return s;
	}
}
