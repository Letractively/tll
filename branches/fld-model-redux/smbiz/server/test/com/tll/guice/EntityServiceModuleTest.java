/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.guice;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tll.TestBase;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;
import com.tll.di.EntityServiceModule;
import com.tll.di.JpaModule;
import com.tll.di.MockEntitiesModule;

/**
 * EntityServiceModuleTest
 * @author jpk
 */
@Test(groups = {
	"bootstrap",
	"entity.service" })
public class EntityServiceModuleTest extends TestBase {

	public void testLoadHibernate() throws Exception {
		final JpaModule jm = new JpaModule(JpaMode.MOCK);
		final DaoModule dm = new DaoModule(DaoMode.ORM);
		final EntityServiceModule esm = new EntityServiceModule();
		final Injector injector = Guice.createInjector(jm, dm, esm);
		assert injector != null;
	}

	public void testLoadMock() throws Exception {
		final MockEntitiesModule mem = new MockEntitiesModule();
		final DaoModule dm = new DaoModule(DaoMode.MOCK);
		final EntityServiceModule esm = new EntityServiceModule();
		final Injector injector = Guice.createInjector(mem, dm, esm);
		assert injector != null;
	}

}
