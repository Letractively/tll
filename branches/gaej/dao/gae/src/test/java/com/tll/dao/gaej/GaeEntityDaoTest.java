/**
 * The Logic Lab
 * @author jpk
 * @since Jan 16, 2010
 */
package com.tll.dao.gaej;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.test.TestEntityDaoTestHandler;
import com.tll.di.GaejDaoModule;
import com.tll.di.test.TestPersistenceUnitModule;


/**
 * GaeEntityDaoTest
 * @author jpk
 */
@Test
public class GaeEntityDaoTest extends AbstractGaejEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] {
			new TestEntityDaoTestHandler()
		};
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new TestPersistenceUnitModule());
		modules.add(new GaejDaoModule(getConfig()));
	}
}
