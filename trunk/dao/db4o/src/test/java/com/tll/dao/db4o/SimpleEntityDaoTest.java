package com.tll.dao.db4o;

import org.testng.annotations.Test;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;




/**
 * SimpleEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "db4o" })
public class SimpleEntityDaoTest extends AbstractDb4oEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() };
	}

	@Override
	protected Class<? extends IEntityGraphPopulator> getEntityGraphPopulator() {
		return TestPersistenceUnitEntityGraphBuilder.class;
	}
}
