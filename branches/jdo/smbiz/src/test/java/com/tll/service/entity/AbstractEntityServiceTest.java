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
import com.tll.di.EGraphModule;
import com.tll.di.EntityServiceFactoryModule;
import com.tll.di.JdoDaoModule;
import com.tll.di.ModelModule;
import com.tll.model.EntityBeanFactory;

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
		modules.add(new ModelModule() {

			@Override
			protected void bindEntityAssembler() {
			}
		});
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
			}
		});
		modules.add(new JdoDaoModule(getConfig()));
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

	protected final EntityBeanFactory getMockEntityFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	protected final IEntityServiceFactory getEntityServiceFactory() {
		return injector.getInstance(IEntityServiceFactory.class);
	}
}
