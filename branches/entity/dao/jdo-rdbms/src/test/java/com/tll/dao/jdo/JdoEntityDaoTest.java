package com.tll.dao.jdo;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;
import com.tll.di.JdoDaoModule;
import com.tll.di.test.JdoTransModule;
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
		modules.add(new JdoDaoModule(getConfig()));
		modules.add(new JdoTransModule());
		modules.add(new TestPersistenceUnitModule());
	}

}
