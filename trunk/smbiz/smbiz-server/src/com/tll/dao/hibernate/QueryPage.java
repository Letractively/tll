package com.tll.dao.hibernate;

import java.util.List;

import javax.persistence.Query;

import com.tll.listhandler.IPage;
import com.tll.model.IEntity;

/**
 * JPA implementation of {@link IPage} which holds a reference to a
 * {@link Query} instance.
 * @author jpk
 */
final class QueryPage<T> extends HbmPage<T> {

	private static final long serialVersionUID = -7510639350401309092L;

	/**
	 * Constructor
	 * @param pageSize
	 * @param totalSize
	 * @param pageElements
	 * @param pageNumber
	 * @param refType
	 * @param scalar
	 */
	public QueryPage(int pageSize, int totalSize, List<T> pageElements, int pageNumber, Class<? extends IEntity> refType,
			boolean scalar) {
		super(pageSize, totalSize, pageElements, pageNumber, refType, scalar);
	}

	/**
	 * JPA Query.
	 */
	private Query q;

	/**
	 * @return the query to set
	 */
	public Query getQuery() {
		return q;
	}

	/**
	 * @param q the query to set
	 */
	public void setQuery(Query q) {
		this.q = q;
	}
}
