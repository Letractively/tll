/*
 * The Logic Lab
 */
package com.tll.service.entity;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.AbstractDbAwareTest;
import com.tll.dao.IEntityDao;
import com.tll.di.DbDialectModule;
import com.tll.di.EntityAssemblerModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.di.OrmDaoModule;
import com.tll.di.TransactionModule;
import com.tll.di.ValidationModule;
import com.tll.model.MockEntityFactory;

/**
 * AbstractEntityServiceTest - Base class for all entity service related testing
 * @author jpk
 */
@Test(groups = "service.entity")
public abstract class AbstractEntityServiceTest extends AbstractDbAwareTest {

	/**
	 * Constructor
	 */
	public AbstractEntityServiceTest() {
		super();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ValidationModule());
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		modules.add(new DbDialectModule(getConfig()));
		modules.add(new OrmDaoModule(getConfig()));
		modules.add(new TransactionModule(getConfig()));
		modules.add(new EntityAssemblerModule() {

			@Override
			protected void bindEntityAssembler() {
				// TODO
			}
		});
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
		getDbSupport().getDbShell().create();
	}

	@Override
	protected void beforeMethod() {
		super.beforeMethod();
		getDbSupport().getDbShell().clear(); // reset
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
