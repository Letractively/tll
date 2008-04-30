/**
 * The Logic Lab
 * @author jpk
 * Mar 10, 2008
 */
package com.tll.dao.hibernate;

import java.util.List;

import com.tll.listhandler.Page;
import com.tll.model.IEntity;

/**
 * HbmPage - Hibernate IPage impl used for hibernate managed result set paging.
 * @author jpk
 */
abstract class HbmPage<T> extends Page<T> {

	final Class<? extends IEntity> refType;
	final boolean scalar;

	/**
	 * Constructor
	 * @param pageSize
	 * @param totalSize
	 * @param pageElements
	 * @param pageNumber
	 * @param refType
	 * @param scalar
	 */
	HbmPage(int pageSize, int totalSize, List<T> pageElements, int pageNumber, Class<? extends IEntity> refType,
			boolean scalar) {
		super(pageSize, totalSize, pageElements, pageNumber);
		this.refType = refType;
		this.scalar = scalar;
	}

}
