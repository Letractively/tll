package com.tll.dao.gaej;

import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.gaej.test.GaejTestDaoDecorator;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractGaejEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "gaej" })
public abstract class AbstractGaejEntityDaoTest extends AbstractEntityDaoTest<GaejEntityDao, GaejTestDaoDecorator> {

	/**
	 * A distinct db shell.
	 */
	//protected JdbcDbShell dbShell;

	/**
	 * Constructor
	 */
	public AbstractGaejEntityDaoTest() {
		super(GaejTestDaoDecorator.class);
	}

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return null;
	}

	/*
	 * We must have create a jdo db shell BEFORE the test injector is created
	 * as the PersistenceManagerFactory will "ping" the test db and if it doesn't exist we fail fast.
	 */
	/*
	@Override
	protected IDbShell getDbShell() {
		if(dbShell == null) {
			// create a distinct instance NOT using the test injector
			dbShell = (JdbcDbShell) Guice.createInjector(new JdbcDbShellModule(getConfig())).getInstance(IDbShell.class);
			assert dbShell != null;
		}
		return dbShell;
	}
	*/
	
	@Override
	protected final IEntity getEntityFromDb(PrimaryKey<IEntity> key) {
		// TODO verify we always hit the db
		return dao.load(key);
	}
}
