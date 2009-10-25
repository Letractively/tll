package com.tll.dao.db4o;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;
import com.tll.di.test.TestDb4oDaoModule;
import com.tll.di.test.TestPersistenceUnitModelModule;

/**
 * SimpleEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "db4o"
})
public class SimpleEntityDaoTest extends AbstractDb4oEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] {
			new TestEntityDaoTestHandler()
		};
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new TestPersistenceUnitModelModule());
		modules.add(new TestDb4oDaoModule(getConfig()));
	}

}
