package com.tll.dao.gae;

import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbShell;
import com.tll.dao.gae.test.GaeDatastoreTestEnvironment;
import com.tll.dao.gae.test.GaeTestDaoDecorator;
import com.tll.model.IEntity;

/**
 * AbstractGaeEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "gae" })
public abstract class AbstractGaeEntityDaoTest extends AbstractEntityDaoTest<GaeEntityDao, GaeTestDaoDecorator> {

	/**
	 * A distinct db shell.
	 */
	protected GaeShell dbShell;

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
	@Override
	protected IDbShell getDbShell() {
		if(dbShell == null) {
			// create a distinct instance NOT using the test injector
			// TODO currently the gae shell doesn't do anything
			dbShell = (GaeShell) Guice.createInjector(new Module() {

				@Override
				public void configure(Binder binder) {
					binder.bind(IDbShell.class).toInstance(new GaeShell());
				}
			}).getInstance(IDbShell.class);
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
