/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.jdo;

import org.testng.annotations.Test;

import com.tll.dao.DaoHandlersProvider;
import com.tll.dao.IEntityDaoTestHandler;

/**
 * JdoEntityDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class JdoEntityDaoTest extends AbstractJdoEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return DaoHandlersProvider.getHandlers();
	}

}
