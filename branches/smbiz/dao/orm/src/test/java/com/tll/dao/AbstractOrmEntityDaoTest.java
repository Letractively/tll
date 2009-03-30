/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.DbTestSupport;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.DbDialectModule;
import com.tll.di.DbShellModule;
import com.tll.di.OrmDaoModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractOrmEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "orm" })
public abstract class AbstractOrmEntityDaoTest extends AbstractEntityDaoTest {
	
	private final DbTestSupport dbSupport;

	/**
	 * Constructor
	 */
	public AbstractOrmEntityDaoTest() {
		super();
		dbSupport = new DbTestSupport();
	}

	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new OrmDaoModule());
	}

	@Override
	protected final void doBeforeClass() {
		// create a db shell to ensure db exists and stubbed
		final Injector i = Guice.createInjector(Stage.DEVELOPMENT, new DbDialectModule(), new DbShellModule());
		final DbShell dbShell = i.getInstance(DbShell.class);
		dbShell.create();
		dbShell.clear();
	}

	@Override
	protected final IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		 return DbTestSupport.getEntityFromDb(dao, key);
	}

	@Override
	protected final void endTransaction() {
		dbSupport.endTransaction();
	}

	@Override
	protected final boolean isTransStarted() {
		return dbSupport.isTransStarted();
	}

	@Override
	protected final void setComplete() {
		dbSupport.setComplete();
	}

	@Override
	protected final void startNewTransaction() {
		dbSupport.startNewTransaction();
	}
}
