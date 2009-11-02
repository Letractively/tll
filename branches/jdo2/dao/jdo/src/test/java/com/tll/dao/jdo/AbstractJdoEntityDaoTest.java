package com.tll.dao.jdo;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbShell;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.jdo.test.JdoTestDaoDecorator;
import com.tll.di.test.JdbcDbShellModule;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractJdoEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "jdo"
})
public abstract class AbstractJdoEntityDaoTest extends AbstractEntityDaoTest<JdoEntityDao, JdoTestDaoDecorator> {

	/**
	 * A distinct db shell.
	 */
	protected JdbcDbShell dbShell;

	/**
	 * Constructor
	 */
	public AbstractJdoEntityDaoTest() {
		super(JdoTestDaoDecorator.class);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new JdbcDbShellModule(getConfig()));
	}

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return null;
	}

	/*
	 * We must have create a jdo db shell BEFORE the test injector is created
	 * as the PersistenceManagerFactory will "ping" the test db and if it doesn't exist we fail fast.
	 */
	@Override
	protected IDbShell getDbShell() {
		if(dbShell == null) {
			// create a distinct instance NOT using the test injector
			dbShell = (JdbcDbShell) Guice.createInjector(new JdbcDbShellModule(getConfig())).getInstance(IDbShell.class);
			assert dbShell != null;
		}
		return dbShell;
	}

	@Override
	protected final IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		return dao.load(key);
	}
}
