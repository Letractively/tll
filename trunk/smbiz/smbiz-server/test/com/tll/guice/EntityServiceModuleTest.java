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

/**
 * EntityServiceModuleTest
 * @author jpk
 */
@Test(groups = "service")
public class EntityServiceModuleTest extends TestBase {

	public void testLoadHibernate() throws Exception {
		JpaModule jm = new JpaModule(JpaMode.MOCK);
		DaoModule dm = new DaoModule(DaoMode.HIBERNATE);
		EntityServiceModule esm = new EntityServiceModule();
		Injector injector = Guice.createInjector(jm, dm, esm);
		assert injector != null;
	}

	public void testLoadMock() throws Exception {
		MockEntitiesModule mem = new MockEntitiesModule();
		DaoModule dm = new DaoModule(DaoMode.MOCK);
		EntityServiceModule esm = new EntityServiceModule();
		Injector injector = Guice.createInjector(mem, dm, esm);
		assert injector != null;
	}

}
