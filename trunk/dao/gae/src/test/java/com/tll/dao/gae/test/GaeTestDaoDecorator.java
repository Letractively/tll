/**
 * The Logic Lab
 * @author jpk
 * @since Oct 29, 2009
 */
package com.tll.dao.gae.test;

import javax.jdo.PersistenceManagerFactory;

import com.tll.dao.gae.GaeEntityDao;
import com.tll.dao.test.EntityDaoTestDecorator;

/**
 * GaeTestDaoDecorator
 * @author jpk
 */
public final class GaeTestDaoDecorator extends EntityDaoTestDecorator<GaeEntityDao> {

	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return rawDao.getPersistenceManagerFactory();
	}
}
