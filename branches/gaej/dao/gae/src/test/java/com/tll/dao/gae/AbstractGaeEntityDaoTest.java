package com.tll.dao.gae;

import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.gae.GaeEntityDao;
import com.tll.dao.gae.test.GaeDatastoreTestEnvironment;
import com.tll.dao.gae.test.GaeTestDaoDecorator;
import com.tll.model.IEntity;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractGaeEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "gae" })
public abstract class AbstractGaeEntityDaoTest extends AbstractEntityDaoTest<GaeEntityDao, GaeTestDaoDecorator> {

	/**
	 * A distinct db shell.
	 */
	//protected JdbcDbShell dbShell;

	/**
	 * Constructor
	 */
	public AbstractGaeEntityDaoTest() {
		super(GaeTestDaoDecorator.class);
		// create the gae test env
		setTestEnv(new GaeDatastoreTestEnvironment());
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
