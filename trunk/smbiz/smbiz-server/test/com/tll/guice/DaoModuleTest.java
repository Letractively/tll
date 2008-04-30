/**
 * The Logic Lab
 */
package com.tll.guice;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tll.TestBase;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;

/**
 * DaoModuleTest - Verifies loading of the DAO module
 * @author jpk
 */
@Test(groups = "dao")
public class DaoModuleTest extends TestBase {

	public void testLoadHibernate() throws Exception {
		JpaModule jm = new JpaModule(JpaMode.MOCK);
		DaoModule dm = new DaoModule(DaoMode.HIBERNATE);
		Injector injector = Guice.createInjector(jm, dm);
		assert injector != null;
	}

	public void testLoadMock() throws Exception {
		MockEntitiesModule mem = new MockEntitiesModule();
		DaoModule dm = new DaoModule(DaoMode.MOCK);
		Injector injector = Guice.createInjector(mem, dm);
		assert injector != null;
	}

}
