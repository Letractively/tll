/*
 * The Logic Lab
 */
package com.tll.service.entity;

import java.util.List;

import net.sf.ehcache.CacheManager;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.db4o.ObjectContainer;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.dao.IDbTrans;
import com.tll.dao.IEntityDao;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.di.AbstractDb4oDaoModule;
import com.tll.di.SmbizDb4oDaoModule;
import com.tll.di.SmbizEGraphModule;
import com.tll.di.SmbizEntityServiceFactoryModule;
import com.tll.di.SmbizModelModule;
import com.tll.di.SmbizEntityServiceFactoryModule.UserCacheAware;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.model.EntityBeanFactory;
import com.tll.model.IEntity;
import com.tll.util.ClassUtil;

/**
 * AbstractEntityServiceTest - Base class for all entity service related testing
 * @author jpk
 */
@Test(groups = "service.entity")
public abstract class AbstractEntityServiceTest extends AbstractDbAwareTest {

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);

		// satisfy caching requirement for UserService
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(CacheManager.class).annotatedWith(UserCacheAware.class).toProvider(new Provider<CacheManager>() {

					@Override
					public CacheManager get() {
						return new CacheManager(ClassUtil.getResource("ehcache-smbiz-persist.xml"));
					}
				}).in(Scopes.SINGLETON);
			}
		});

		modules.add(new SmbizModelModule());
		modules.add(new SmbizEGraphModule());
		modules.add(new SmbizDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IDbTrans.class).to(Db4oTrans.class).in(Scopes.SINGLETON);
			}
		});
		modules.add(new SmbizEntityServiceFactoryModule());
	}

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("db4o-config.properties"));
	}

	@BeforeClass(alwaysRun = true)
	public void onBeforeClass() {
		beforeClass();
	}

	@AfterClass(alwaysRun = true)
	public final void onAfterClass() {
		afterClass();
	}

	@BeforeMethod
	public void onBeforeMethod() {
		beforeMethod();
	}

	@Override
	protected void beforeClass() {
		// create the db shell first (before test injector creation) to avoid db4o
		// file lock when objectcontainer is instantiated
		final Config cfg = new Config();
		cfg.addProperty(AbstractDb4oDaoModule.ConfigKeys.DB4O_FILENAME.getKey(), getConfig().getProperty(
				AbstractDb4oDaoModule.ConfigKeys.DB4O_FILENAME.getKey()));
		cfg.addProperty(AbstractDb4oDaoModule.ConfigKeys.DB_TRANS_TIMEOUT.getKey(), getConfig().getProperty(
				AbstractDb4oDaoModule.ConfigKeys.DB_TRANS_TIMEOUT.getKey()));
		cfg.setProperty(AbstractDb4oDaoModule.ConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), Boolean.FALSE);
		final Injector i = buildInjector(new SmbizDb4oDaoModule(cfg), new Db4oDbShellModule());
		final IDbShell dbs = i.getInstance(IDbShell.class);

		dbs.drop();
		dbs.create();
		super.beforeClass();
	}

	@Override
	protected void afterClass() {
		injector.getInstance(ObjectContainer.class).close();
		injector.getInstance(CacheManager.class).shutdown();
	}

	@Override
	protected void beforeMethod() {
		super.beforeMethod();
		// reset data
		getDbShell().clearData();
		getDbShell().addData();
	}

	/**
	 * Stubs an entity optionally persisting it.
	 * @param etype
	 * @param persist
	 * @return The newly created entity
	 */
	protected <E extends IEntity> E stub(Class<E> etype, boolean persist) {
		final E e = getEntityBeanFactory().getEntityCopy(etype, false);
		if(persist) getDao().persist(e);
		return e;
	}

	protected final IEntityDao getDao() {
		return injector.getInstance(IEntityDao.class);
	}

	protected final EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	protected final IEntityServiceFactory getEntityServiceFactory() {
		return injector.getInstance(IEntityServiceFactory.class);
	}
}