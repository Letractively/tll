package com.tll.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.tll.criteria.ICriteria;
import com.tll.listhandler.IPage;
import com.tll.model.IEntity;

/**
 * {@link IPage} impl holding a {@link DetachedCriteria} instance to facilitate
 * faster subsequent page requests.
 * @author jpk
 */
final class DetachedCriteriaPage<T> extends HbmPage<T> {

	private static final long serialVersionUID = -7718707220286771374L;

	/**
	 * Hibernate {@link DetachedCriteria}.
	 */
	private DetachedCriteria dc;

	/**
	 * Constructor
	 * @param pageSize
	 * @param totalSize
	 * @param pageElements
	 * @param pageNumber
	 * @param refType
	 * @param scalar
	 */
	public DetachedCriteriaPage(int pageSize, int totalSize, List<T> pageElements, int pageNumber,
			Class<? extends IEntity> refType, boolean scalar) {
		super(pageSize, totalSize, pageElements, pageNumber, refType, scalar);
	}

	/**
	 * @return The Hibernate detached criteria representing the previously
	 *         translated {@link ICriteria}.
	 */
	public DetachedCriteria getDetachedCriteria() {
		return dc;
	}

	/**
	 * Sets the hibernate detached criteria.
	 */
	public void setDetachedCriteria(DetachedCriteria dc) {
		this.dc = dc;
	}
}
