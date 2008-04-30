package com.tll.criteria;

import java.io.Serializable;
import java.util.Map;

import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;

/**
 * A composite of {@link Criterion} objects.
 * @author jpk
 */
public interface ICriteria<E extends IEntity> extends Serializable, Cloneable {

	/**
	 * The default page size.
	 */
	static final int DEFAULT_PAGE_SIZE = 25;

	/**
	 * @return The {@link CriteriaType} of this criteria.
	 */
	CriteriaType getCriteriaType();

	/**
	 * Sets the criteria type.
	 * @param criteriaType
	 */
	void setCriteriaType(CriteriaType criteriaType);

	/**
	 * @return entity class
	 */
	Class<? extends E> getEntityClass();

	/**
	 * Sets the entity class.
	 * @param entityClass
	 */
	void setEntityClass(Class<? extends E> entityClass);

	/**
	 * @return The name of a named query defined in the persistence layer. May be
	 *         <code>null</code>.
	 */
	String getQueryName();

	/**
	 * Sets the query name
	 * @param queryName
	 * @param isScalarQuery Does the referenced query return scalar (non-entity)
	 *        results?
	 */
	void setQueryName(String queryName, boolean isScalarQuery);

	/**
	 * Retrieves any named query parameters if a named query is specified.
	 * @return Map of the named query params and their associated String wise
	 *         values. May be <code>null</code>.
	 */
	Map<String, String> getQueryParams();

	/**
	 * Sets a query parameter for a named query.
	 * @param paramName The parameter name matching the query param name
	 * @param paramValue The parameter value in String form
	 */
	void setQueryParam(String paramName, String paramValue);

	/**
	 * @return true if at least one valid {@link Criterion} exists.
	 */
	boolean isSet();

	/**
	 * clear all the {@link Criterion}.
	 */
	void clear();

	/**
	 * @return the primary entityGroup.
	 */
	ICriterionGroup getPrimaryGroup();

	/**
	 * Returns an array of field names that will be used to order the result set.
	 * For multiple values, the ordering will be applied in the order the field
	 * names are placed in the array.
	 * @return String array of field names
	 */
	Sorting getSorting();

	/**
	 * Allow sorting to be set for search-based list handling
	 */
	void setSorting(Sorting sorting);

	/**
	 * @return The desired page size for single page display of multi-page lists.
	 *         Used for list handling.
	 */
	int getPageSize();

	/**
	 * Set the page size.
	 * @param pageSize
	 */
	void setPageSize(int pageSize);

	/**
	 * Deep copies this criteria.
	 * @return a deep copy.
	 */
	ICriteria<E> copy();
}
