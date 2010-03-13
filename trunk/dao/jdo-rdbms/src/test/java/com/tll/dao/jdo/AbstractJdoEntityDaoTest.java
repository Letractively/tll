package com.tll.dao.jdo;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbShell;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.jdo.test.JdoTestDaoDecorator;
import com.tll.di.test.JdbcDbShellModule;
import com.tll.model.IEntity;

/**
 * AbstractJdoEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "jdo"
})
public abstract class AbstractJdoEntityDaoTest extends AbstractEntityDaoTest<JdoRdbmsEntityDao, JdoTestDaoDecorator> {

	/**
	 * A distinct db shell.
	 */
	protected IDbShell dbShell;

	/**
	 * Constructor
	 */
	public AbstractJdoEntityDaoTest() {
		super(JdoTestDaoDecorator.class);
	}

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return null;
	}

	/*
	 * We must create a jdo db shell BEFORE the test injector is created
	 * as the PersistenceManagerFactory will "ping" the test db and if it doesn't exist we fail fast.
	 */
	@Override
	protected IDbShell getDbShell() {
		if(dbShell == null) {
			// create a distinct instance NOT using the test injector
			dbShell = Guice.createInjector(new JdbcDbShellModule(getConfig())).getInstance(IDbShell.class);
			assert dbShell != null;
		}
		return dbShell;
	}

	@Override
	protected final <E extends IEntity> E getEntityFromDb(Class<E> entityType, Object pk) {
		// TODO verify we always hit the db
		return dao.load(entityType, pk);
	}
}
