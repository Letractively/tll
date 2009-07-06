/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.jdo;

import javax.jdo.PersistenceManager;

import org.testng.annotations.Test;

import com.tll.dao.DaoHandlersProvider;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.jdo.AbstractJdoEntityDaoTest;

/**
 * MockEntityDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class JdoEntityDaoTest extends AbstractJdoEntityDaoTest {

	/**
	 * Constructor
	 * @param pm
	 */
	public JdoEntityDaoTest(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return DaoHandlersProvider.getHandlers();
	}

}
