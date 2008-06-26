/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.client.search;

import com.tll.criteria.CriteriaType;
import com.tll.model.EntityType;

/**
 * EntitySearch
 * @author jpk
 */
public abstract class EntitySearch extends SearchBase {

	/**
	 * Corres. to the string-wise representation of the EntityType enum.
	 */
	protected EntityType entityType;

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
	public EntitySearch(CriteriaType criteriaType, EntityType entityType) {
		super(criteriaType);
		this.entityType = entityType;
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 * @param entityType
	 */
	public EntitySearch(String businessKeyName, EntityType entityType) {
		super();
		this.entityType = entityType;
		this.businessKeyName = businessKeyName;
	}

	public final EntityType getEntityType() {
		return entityType;
	}

	public final void setEntityType(EntityType entityType) {
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
