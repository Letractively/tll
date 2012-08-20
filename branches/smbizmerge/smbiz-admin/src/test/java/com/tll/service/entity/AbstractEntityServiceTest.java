/*
 * The Logic Lab
 */
package com.tll.service.entity;

import java.net.URI;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.testng.annotations.Test;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.IDbShell;
import com.tll.dao.IDbTrans;
import com.tll.dao.IEntityDao;
import com.tll.dao.db4o.AbstractDb4oDaoModule;
import com.tll.dao.db4o.AbstractDb4oDaoModule.Db4oFile;
import com.tll.dao.db4o.Db4oDbShell;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.server.SmbizDb4oPersistModule;

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

	@Override
	protected void beforeClass() {
		// re-create db
		final Injector i = buildInjector(new Module() {
			
			@Override
			public void configure(Binder binder) {
				binder.bind(Key.get(URI.class, Db4oFile.class)).toProvider(new Provider<URI>() {
					
					@Override
					public URI get() {
						String dbPath = getConfig().getString(AbstractDb4oDaoModule.ConfigKeys.DB4O_FILENAME.getKey());
						return AbstractDb4oDaoModule.getDb4oClasspathFileRef(dbPath);
					}
				}).in(Scopes.SINGLETON);
			}
		}, new Db4oDbShellModule());
		IDbShell dbShell = i.getInstance(IDbShell.class);
		dbShell.drop();
		dbShell.create();
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
		//Db4oDbShell dbShell = (Db4oDbShell) getDbShell();
		EmbeddedObjectContainer dbSession = injector.getInstance(EmbeddedObjectContainer.class);
		Db4oDbShell.clearData(dbSession);
		//dbShell.addData(dbSession);
	}

	/**
	 * Stubs an entity optionally persisting it.
	 * @param etype
	 * @param persist
	 * @return The newly created entity
	 */
	protected <E extends IEntity> E stub(Class<E> etype, boolean persist) {
		final E e = getEntityBeanFactory().getEntityCopy(etype);
		if(persist) getDao().persist(e);
		return e;
	}

	protected final IEntityFactory getEntityFactory() {
		return injector.getInstance(IEntityFactory.class);
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
