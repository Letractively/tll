/**
 * The Logic Lab
 * @author jpk
 * Jan 21, 2009
 */
package com.tll.criteria;

/**
 * ISelectNamedQueryDef
 * @author jpk
 */
public interface ISelectNamedQueryDef {

	/**
	 * @return the entityType
	 */
	Class<?> getEntityType();

	/**
	 * @return the scalar
	 */
	boolean isScalar();
	
	/**
	 * @return the queryName
	 */
	String getQueryName();
}
