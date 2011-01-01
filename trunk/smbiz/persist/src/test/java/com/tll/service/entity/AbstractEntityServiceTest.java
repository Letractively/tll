/*
 * The Logic Lab
 */
package com.tll.service.entity;

import java.io.File;
import java.net.URI;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.SmbizDb4oPersistModule;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbTrans;
import com.tll.dao.IEntityDao;
import com.tll.dao.db4o.AbstractDb4oDaoModule.Db4oFile;
import com.tll.dao.db4o.Db4oDbShell;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.dao.db4o.test.TestDb4oDaoModule;
import com.tll.model.IEntity;
import com.tll.model.egraph.EntityBeanFactory;

/**
 * AbstractEntityServiceTest - Base class for all entity service related testing
 * @author jpk
 */
@Test(groups = "service.entity")
public abstract class AbstractEntityServiceTest extends AbstractDbAwareTest {

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizDb4oPersistModule(getConfig()));
		
		// test related
		modules.add(new Db4oDbShellModule());
		modules.add(new Module() {
			
			@Override
			public void configure(Binder binder) {
				binder.bind(IDbTrans.class).to(Db4oTrans.class).in(Scopes.SINGLETON);
			}
		});
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
		// kill the existing db4o file if present
		final Config cfg = Config.load();
		final Injector i = buildInjector(new TestDb4oDaoModule(cfg));
		final File f = new File(i.getInstance(Key.get(URI.class, Db4oFile.class)));
		f.delete();
		super.beforeClass();
	}

	@Override
	protected void afterClass() {
		super.afterClass();
		Db4oDbShell dbShell = (Db4oDbShell) getDbShell();
		dbShell.killDbSession(injector.getInstance(EmbeddedObjectContainer.class));
		injector.getInstance(CacheManager.class).shutdown();
	}

	@Override
	protected void beforeMethod() {
		super.beforeMethod();
		// reset data
		Db4oDbShell dbShell = (Db4oDbShell) getDbShell();
		EmbeddedObjectContainer dbSession = injector.getInstance(EmbeddedObjectContainer.class);
		Db4oDbShell.clearData(dbSession);
		dbShell.addData(dbSession);
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
