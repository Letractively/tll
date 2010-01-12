/**
 * The Logic Lab
 * @author jpk
 * @since Oct 29, 2009
 */
package com.tll.dao.gaej.test;

import javax.jdo.PersistenceManagerFactory;

import com.tll.dao.gaej.GaejEntityDao;
import com.tll.dao.test.EntityDaoTestDecorator;

/**
 * GaejTestDaoDecorator
 * @author jpk
 */
public final class GaejTestDaoDecorator extends EntityDaoTestDecorator<GaejEntityDao> {

	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return rawDao.getPersistenceManagerFactory();
	}
}
