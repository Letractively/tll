package com.tll.criteria;

import java.io.Serializable;
import java.util.List;

import com.tll.model.IEntity;

/**
 * A composite of {@link Criterion} objects.
 * @author jpk
 * @param <E>
 */
public interface ICriteria<E extends IEntity> extends Serializable {

	/**
	 * @return The {@link CriteriaType} of this criteria.
	 */
	CriteriaType getCriteriaType();

	/**
	 * @return entity class
	 */
	Class<E> getEntityClass();

	/**
	 * @return the primary entityGroup.
	 */
	CriterionGroup getPrimaryGroup();

	/**
	 * @return The named query definition.
	 */
	ISelectNamedQueryDef getNamedQueryDefinition();

	/**
	 * Retrieves any query parameters if a named query is specified.
	 * @return Collection of query params and their associated String wise values.
	 *         May be <code>null</code> or empty.
	 */
	List<IQueryParam> getQueryParams();

	/**
	 * @return true if at least one valid {@link Criterion} exists.
	 */
	boolean isSet();

	/**
	 * clear all the {@link Criterion}.
	 */
	void clear();
}
