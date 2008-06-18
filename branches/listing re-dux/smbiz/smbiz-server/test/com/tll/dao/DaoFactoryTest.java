/**
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.annotations.Test;

import com.google.inject.Injector;
import com.tll.SystemError;
import com.tll.TestBase;
import com.tll.guice.DaoModule;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * DaoFactoryTest
 * @author jpk
 */
@Test(groups = "dao.factory")
public class DaoFactoryTest extends TestBase {

	/**
	 * Constructor
	 */
	public DaoFactoryTest() {
		super();
	}

	@Test
	public void testHibernate() throws Exception {
		doTest(DaoMode.ORM);
	}

	@Test
	public void testMock() throws Exception {
		doTest(DaoMode.MOCK);
	}

	private void doTest(DaoMode daoMode) throws Exception {
		final Injector injector = buildStaticInjector(new DaoModule(daoMode));
		final IDaoFactory df = injector.getInstance(IDaoFactory.class);
		assert df != null;
		Class<? extends IEntity>[] entityClasses = EntityUtil.getEntityClasses();
		for(Class<? extends IEntity> entityClass : entityClasses) {
			IEntityDao<? extends IEntity> ed = null;
			try {
				ed = df.instanceByEntityType(entityClass);
			}
			catch(SystemError se) {
				logger.debug("No " + daoMode.name() + " DAO found for entity type: " + entityClass);
				continue;
			}
			assert ed != null;
			Class<? extends IEntity> dec = ed.getEntityClass();
			assert dec != null;
			assert dec.isAssignableFrom(entityClass);
		}
	}

}
