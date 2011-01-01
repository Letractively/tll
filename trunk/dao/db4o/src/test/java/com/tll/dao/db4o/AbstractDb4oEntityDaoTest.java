package com.tll.dao.db4o;

import java.net.URI;
import java.util.List;

import org.testng.annotations.Test;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.tll.dao.AbstractEntityDaoTest;
import com.tll.dao.IDbTrans;
import com.tll.dao.db4o.AbstractDb4oDaoModule.Db4oFile;
import com.tll.dao.db4o.test.Db4oDbShellModule;
import com.tll.dao.db4o.test.Db4oTestDaoDecorator;
import com.tll.dao.db4o.test.Db4oTrans;
import com.tll.model.IEntity;

/**
 * AbstractDb4oEntityDaoTest
 * @author jpk
 */
@Test(groups = {
	"dao", "db4o" })
public abstract class AbstractDb4oEntityDaoTest extends AbstractEntityDaoTest<Db4oEntityDao, Db4oTestDaoDecorator> {

	private Db4oTrans dbtrans;

	/**
	 * Constructor
	 */
	public AbstractDb4oEntityDaoTest() {
		super(Db4oTestDaoDecorator.class);
	}

	@Override
	protected void resetDb() {
		getDbShell().drop();
		getDbShell().create();
	}

	/*
	 * We have to reverse the order of operations for db4o dao test prep
	 * as the Db4oDbShell is dependent on the EmbeddedObjectContainer!
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
	protected <E extends IEntity> E getEntityFromDb(Class<E> entityType, Long pk) {
		return dao.load(entityType, pk);
	}

	@Override
	protected final IDbTrans getDbTrans() {
		if(dbtrans == null) {
			dbtrans = new Db4oTrans((EmbeddedObjectContainer) dao.getObjectContainer());
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
		getDbTrans().setComplete();
		getDbTrans().endTrans();
		dao.getObjectContainer().close();
		final EmbeddedConfiguration c = injector.getInstance(EmbeddedConfiguration.class);
		final URI db4oUri = injector.getInstance(Key.get(URI.class, Db4oFile.class));
		final EmbeddedObjectContainer oc = Db4oEmbedded.openFile(c, db4oUri.getPath());
		dao.setObjectContainer(oc);
		((Db4oTrans) getDbTrans()).setObjectContainer(oc);
		((Db4oEntityFactory) getEntityFactory()).setObjectContainer(new Provider<EmbeddedObjectContainer>() {

			@Override
			public EmbeddedObjectContainer get() {
				return oc;
			}
		});
		getDbTrans().startTrans();
		final IEntity eloaded = dao.load(e.entityClass(), e.getId());
		entityHandler.verifyLoadedEntityState(eloaded);
		dao.purge(eloaded);
		getDbTrans().setComplete();
		getDbTrans().endTrans();
	}
}
