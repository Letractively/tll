/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.orm;

import org.testng.annotations.Test;

import com.tll.dao.DaoHandlersProvider;
import com.tll.dao.IEntityDaoTestHandler;

/**
 * MockEntityDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class OrmEntityDaoTest extends AbstractOrmEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return DaoHandlersProvider.getHandlers();
	}

}
