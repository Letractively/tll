/**
 * The Logic Lab
 * @author jpk
 * Mar 5, 2008
 */
package com.tll.dao.hibernate;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.tll.config.Config;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DaoModule;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.JpaModule;

/**
 * HibernateEnvironmentTest - Verifies hibernate loads error free
 * @author jpk
 */
@Test(groups = "dao.hibernate")
public class HibernateEnvironmentTest {
	
	private DbShell db;
	
	@BeforeClass
	public void init() {
		Injector i = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule());
		db = i.getInstance(DbShell.class);
		db.create();
	}

	@AfterClass
	public void destroy() {
		db.delete();
		db = null;
	}

	/**
	 * Verifies the loading of the Hibernate environment.
	 */
	public void test() {
		Config.instance().setProperty(JpaModule.ConfigKeys.JPA_MODE_PARAM.getKey(), JpaMode.LOCAL.toString());
		Config.instance().setProperty(DaoModule.ConfigKeys.DAO_MODE_PARAM.getKey(), DaoMode.ORM.toString());
		Guice.createInjector(Stage.DEVELOPMENT, new JpaModule(), new DaoModule());
	}
}
