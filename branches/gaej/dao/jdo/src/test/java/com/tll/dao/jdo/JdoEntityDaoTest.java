package com.tll.dao.jdo;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;
import com.tll.di.test.TestJdoDaoModule;
import com.tll.di.test.TestPersistenceUnitModule;

/**
 * JdoEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "jdo" })
public class JdoEntityDaoTest extends AbstractJdoEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] {
			new TestEntityDaoTestHandler()
		};
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new TestJdoDaoModule(getConfig()));
		modules.add(new TestPersistenceUnitModule());
	}

}
