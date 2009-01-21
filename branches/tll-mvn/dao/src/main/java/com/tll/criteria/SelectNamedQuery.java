/**
 * The Logic Lab
 * @author jpk
 * Jan 21, 2009
 */
package com.tll.criteria;

import com.tll.model.IEntity;

/**
 * SelectNamedQuery
 * @author jpk
 */
public class SelectNamedQuery {

	private final String queryName;

	private Class<? extends IEntity> entityType;

	private boolean scalar;

	/**
	 * Constructor
	 * @param queryName
	 * @param entityType
	 * @param scalar
	 */
	public SelectNamedQuery(String queryName, Class<? extends IEntity> entityType, boolean scalar) {
		super();
		this.queryName = queryName;
		this.entityType = entityType;
		this.scalar = scalar;
	}

	/**
	 * @return the entityType
	 */
	public Class<? extends IEntity> getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(Class<? extends IEntity> entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the scalar
	 */
	public boolean isScalar() {
		return scalar;
	}

	/**
	 * @param scalar the scalar to set
	 */
	public void setScalar(boolean scalar) {
		this.scalar = scalar;
	}

	/**
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}
}
