/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.jdo;

import javax.jdo.PersistenceManager;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.TestEntityDaoTestHandler;



/**
 * SimpleEntityDaoTest
 * @author jpk
 */
public class SimpleEntityDaoTest extends AbstractJdoEntityDaoTest {

	/**
	 * Constructor
	 * @param pm
	 */
	public SimpleEntityDaoTest(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() };
	}
}
