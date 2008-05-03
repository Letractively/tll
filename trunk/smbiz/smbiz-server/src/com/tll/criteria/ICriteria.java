package com.tll.criteria;

import java.io.Serializable;
import java.util.Collection;

import com.tll.model.IEntity;

/**
 * A composite of {@link Criterion} objects.
 * @author jpk
 */
public interface ICriteria<E extends IEntity> extends Serializable, Cloneable {

	/**
	 * @return The {@link CriteriaType} of this criteria.
	 */
	CriteriaType getCriteriaType();

	/**
	 * @return entity class
	 */
	Class<? extends E> getEntityClass();

	/**
	 * @return the primary entityGroup.
	 */
	ICriterionGroup getPrimaryGroup();

	/**
	 * @return The named query definition.
	 */
	SelectNamedQuery getNamedQueryDefinition();

	/**
	 * Retrieves any query parameters if a named query is specified.
	 * @return Collection of query params and their associated String wise values.
	 *         May be <code>null</code> or empty.
	 */
	Collection<IQueryParam> getQueryParams();

	/**
	 * @return true if at least one valid {@link Criterion} exists.
	 */
	boolean isSet();

	/**
	 * clear all the {@link Criterion}.
	 */
	void clear();

	/**
	 * Deep copies this criteria.
	 * @return a deep copy.
	 */
	ICriteria<E> copy();
}
