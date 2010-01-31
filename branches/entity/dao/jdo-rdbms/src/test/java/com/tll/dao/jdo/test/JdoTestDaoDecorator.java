/**
 * The Logic Lab
 * @author jpk
 * @since Oct 29, 2009
 */
package com.tll.dao.jdo.test;

import javax.jdo.PersistenceManagerFactory;

import com.tll.dao.jdo.JdoRdbmsEntityDao;
import com.tll.dao.test.EntityDaoTestDecorator;

/**
 * JdoTestDaoDecorator
 * @author jpk
 */
public final class JdoTestDaoDecorator extends EntityDaoTestDecorator<JdoRdbmsEntityDao> {

	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return rawDao.getPersistenceManagerFactory();
	}
}
