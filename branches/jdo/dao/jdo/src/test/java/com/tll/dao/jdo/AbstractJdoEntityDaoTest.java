/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao.jdo;

import java.util.List;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.annotations.PersistenceAware;

import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.DbTestSupport;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbShell;
import com.tll.di.JdoDaoModule;
import com.tll.di.ModelModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractJdoEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "jdo" })
@PersistenceAware
public abstract class AbstractJdoEntityDaoTest extends AbstractEntityDaoTest {

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ModelModule() {

			@Override
			protected void bindEntityAssembler() {
				// not needed
			}
		});
		modules.add(new JdoDaoModule(config));
		modules.add(new Module() {

			@Override
			public void configure(Binder b) {
				b.bind(DbTestSupport.class).toProvider(new Provider<DbTestSupport>() {

					@Inject
					PersistenceManagerFactory pmf;

					@SuppressWarnings("synthetic-access")
					@Override
					public DbTestSupport get() {
						return new DbTestSupport(config, pmf);
					}
				}).in(Scopes.SINGLETON);
			}
		});
	}

	protected final IDbShell getDbShell() {
		return injector.getInstance(IDbShell.class);
	}

	protected final DbTestSupport getDbSupport() {
		return injector.getInstance(DbTestSupport.class);
	}

	@Override
	protected final void doBeforeClass() {
		// create a db shell to ensure db exists and stubbed
		final IDbShell dbShell = getDbShell();
		dbShell.create();
		dbShell.clear();
	}

	@Override
	protected final IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		return DbTestSupport.getEntityFromDb(dao, key);
	}

	@Override
	protected final void endTransaction() {
		getDbSupport().endTransaction();
	}

	@Override
	protected final boolean isTransStarted() {
		return getDbSupport().isTransStarted();
	}

	@Override
	protected final void setComplete() {
		getDbSupport().setComplete();
	}

	@Override
	protected final void startNewTransaction() {
		getDbSupport().startNewTransaction();
	}
}
