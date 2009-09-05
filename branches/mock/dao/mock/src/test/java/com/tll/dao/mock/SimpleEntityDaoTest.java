package com.tll.dao.mock;

import org.testng.annotations.Test;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;




/**
 * SimpleEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "mock" })
public class SimpleEntityDaoTest extends AbstractMockEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() };
	}

	@Override
	protected Class<? extends IEntityGraphPopulator> getEntityGraphPopulator() {
		return TestPersistenceUnitEntityGraphBuilder.class;
	}
}
