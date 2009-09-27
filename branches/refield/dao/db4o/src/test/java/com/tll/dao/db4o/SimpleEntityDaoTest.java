package com.tll.dao.db4o;

import org.testng.annotations.Test;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;




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
}
