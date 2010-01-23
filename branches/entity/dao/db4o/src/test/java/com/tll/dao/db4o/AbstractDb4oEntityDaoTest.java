package com.tll.dao.db4o;

import java.net.URI;
import java.util.List;

import org.testng.annotations.Test;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.google.inject.Key;
import com.google.inject.Module;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbTrans;
import com.tll.dao.db4o.test.Db4oTestDaoDecorator;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.di.AbstractDb4oDaoModule.Db4oFile;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.model.GlobalLongPrimaryKey;
import com.tll.model.IEntity;
import com.tll.model.IPrimaryKey;

/**
 * AbstractDb4oEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "db4o"
})
public abstract class AbstractDb4oEntityDaoTest extends AbstractEntityDaoTest<GlobalLongPrimaryKey, Db4oEntityDao, Db4oTestDaoDecorator> {

	private Db4oTrans dbtrans;

	/**
	 * Constructor
	 */
	public AbstractDb4oEntityDaoTest() {
		super(Db4oTestDaoDecorator.class, true);
	}

	@Override
	protected void resetDb() {
		getDbShell().drop();
		getDbShell().create();
	}

	/*
	 * We have to reverse the order of operations for db4o dao test prep
	 * as the Db4oDbShell is dependent on the ObjectContainer!
	 */
	@Override
	protected void prepare() {
		// build the test injector
		buildTestInjector();

		// reset the db
		resetDb();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new Db4oDbShellModule());
	}

	@Override
	protected void afterClass() {
		dao.getObjectContainer().close();
	}

	@Override
	protected final IEntity getEntityFromDb(IPrimaryKey key) {
		return dao.load(key);
	}

	@Override
	protected final IDbTrans getDbTrans() {
		if(dbtrans == null) {
			dbtrans = new Db4oTrans(dao.getObjectContainer());
		}
		return dbtrans;
	}

	/**
	 * Tests the integrity of the object graph between the open/close boundary of
	 * a db4o session.
	 * @throws Exception
	 */
	void daoDb4oTestOpenCloseLoad() throws Exception {
		final IEntity e = getTestEntity();
		dao.persist(e);
		setComplete();
		endTransaction();
		dao.getObjectContainer().close();
		final Configuration c = injector.getInstance(Configuration.class);
		final URI db4oUri = injector.getInstance(Key.get(URI.class, Db4oFile.class));
		final ObjectContainer oc = Db4o.openFile(c, db4oUri.getPath());
		dao.setObjectContainer(oc);
		((Db4oTrans)getDbTrans()).setObjectContainer(oc);
		startNewTransaction();
		final IEntity eloaded = dao.load(e.getPrimaryKey());
		entityHandler.verifyLoadedEntityState(eloaded);
		dao.purge(eloaded);
		setComplete();
		endTransaction();
	}
}
