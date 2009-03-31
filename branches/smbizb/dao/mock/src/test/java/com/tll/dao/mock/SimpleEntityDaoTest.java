package com.tll.dao.mock;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.TestEntityDaoTestHandler;




/**
 * SimpleEntityDaoTest
 * @author jpk
 */
public class SimpleEntityDaoTest extends AbstractMockEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() };
	}
}
