/**
 * The Logic Lab
 * @author jpk
 * Mar 5, 2008
 */
package com.tll.dao.hibernate;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.TestBase;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.DaoModule;
import com.tll.guice.JpaModule;

/**
 * HibernateEnvironmentTest - Verifies hibernate loads error free
 * @author jpk
 */
@Test(groups = "dao.hibernate")
public class HibernateEnvironmentTest extends TestBase {

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);

	}

	/**
	 * Verifies the loading of the Hibernate environment.
	 */
	public void test() {
		try {
			JpaModule jpaModule = new JpaModule(JpaMode.LOCAL);
			DaoModule daoModule = new DaoModule(DaoMode.HIBERNATE);
			Guice.createInjector(Stage.DEVELOPMENT, jpaModule, daoModule);
		}
		catch(Throwable t) {
			Assert.fail(t.getMessage(), t);
		}

	}
}
