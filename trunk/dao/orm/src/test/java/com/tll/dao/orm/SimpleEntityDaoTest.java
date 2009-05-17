/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.orm;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.TestEntityDaoTestHandler;



/**
 * SimpleEntityDaoTest
 * @author jpk
 */
public class SimpleEntityDaoTest extends AbstractOrmEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() };
	}
}
