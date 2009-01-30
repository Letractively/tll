/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.di;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;

/**
 * EntityServiceModuleTest
 * @author jpk
 */
@Test(groups = {
	"bootstrap", "entity.service" })
public class EntityServiceModuleTest {

	public void testLoadHibernate() throws Exception {
		final JpaModule jm = new JpaModule(JpaMode.MOCK);
		final DaoModule dm = new DaoModule(DaoMode.ORM);
		final EntityServiceImplModule esm = new EntityServiceImplModule();
		final Injector injector = Guice.createInjector(jm, dm, esm);
		assert injector != null;
	}

	public void testLoadMock() throws Exception {
		final MockEntityFactoryModule mem = new MockEntityFactoryModule();
		final DaoModule dm = new DaoModule(DaoMode.MOCK);
		final EntityServiceImplModule esm = new EntityServiceImplModule();
		final Injector injector = Guice.createInjector(mem, dm, esm);
		assert injector != null;
	}

}
