/*
 * The Logic Lab 
 */
package com.tll.service.entity;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.dao.DaoMode;
import com.tll.dao.IEntityDao;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.EntityServiceModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.model.mock.MockEntityFactory;

/**
 * AbstractEntityServiceTest - Base class for all entity service related testing
 * @author jpk
 */
@Test(groups = "service.entity")
public abstract class AbstractEntityServiceTest extends DbTest {

	/**
	 * Constructor
	 */
	public AbstractEntityServiceTest() {
		super(JpaMode.SPRING, true);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		modules.add(new DaoModule(DaoMode.ORM));
		modules.add(new EntityServiceModule());
		modules.add(new EntityServiceFactoryModule());
	}

	@BeforeClass(alwaysRun = true)
	public void onBeforeClass() {
		beforeClass();
	}

	@BeforeMethod
	public void onBeforeMethod() {
		beforeMethod();
	}

	@Override
	protected void beforeClass() {
		super.beforeClass();
		// ensure test db is created and cleared
		getDbShell().create();
	}

	@Override
	protected void beforeMethod() {
		super.beforeMethod();
		getDbShell().clear(); // reset
	}

	protected final IEntityDao getEntityDao() {
		return injector.getInstance(IEntityDao.class);
	}

	protected final MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}
	
	protected final IEntityServiceFactory getEntityServiceFactory() {
		return injector.getInstance(IEntityServiceFactory.class);
	}
}
