/**
 * The Logic Lab
 * @author jpk
 * @since Jan 16, 2010
 */
package com.tll.dao.gae;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.AbstractEntityDaoTestHandler;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.di.GaeDaoModule;
import com.tll.di.test.GaeTransModule;
import com.tll.di.test.TestPersistenceUnitModule;
import com.tll.model.test.Address;

/**
 * GaeEntityDaoTest
 * @author jpk
 */
@Test
public class GaeEntityDaoTest extends AbstractGaeEntityDaoTest {

	static class SimpleEntityDaoTestHandler extends AbstractEntityDaoTestHandler<Address> {

		@Override
		public Class<Address> entityClass() {
			return Address.class;
		}

		@Override
		public void assembleTestEntity(Address e) throws Exception {
		}
	}

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new SimpleEntityDaoTestHandler() };
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new GaeDaoModule(getConfig()));
		modules.add(new TestPersistenceUnitModule(null, GaeEntityFactory.class));
		modules.add(new GaeTransModule());
	}
}