/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.jdo;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.TestEntityDaoTestHandler;
import com.tll.di.EGraphModule;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;



/**
 * SimpleEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "orm" })
public class SimpleEntityDaoTest extends AbstractJdoEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		return new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() };
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphPopulator.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
		super.addModules(modules);
	}

}
