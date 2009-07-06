/*
 * Created on - May 19, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 * 
 */
package com.tll.dao.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTestSupport;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.db.IDbShell;
import com.tll.di.DbDialectModule;
import com.tll.di.JdoDaoModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractJdoEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "orm" })
	public abstract class AbstractJdoEntityDaoTest extends AbstractEntityDaoTest {

	private final DbTestSupport dbSupport;

	/**
	 * Constructor
	 * @param pm the required persistence manager to facilitate local transactions
	 */
	public AbstractJdoEntityDaoTest(PersistenceManager pm) {
		super();
		assert this.config != null;
		dbSupport = new DbTestSupport(config, pm);
	}

	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DbDialectModule(config));
		modules.add(new JdoDaoModule(config));
	}

	@Override
	protected final void doBeforeClass() {
		// create a db shell to ensure db exists and stubbed
		final IDbShell dbShell = dbSupport.getDbShell();
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
