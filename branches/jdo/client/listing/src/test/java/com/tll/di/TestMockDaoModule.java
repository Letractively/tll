/**
 * The Logic Lab
 * @author jpk
 * @since Jun 25, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;


/**
 * TestMockDaoModule
 * @author jpk
 */
public class TestMockDaoModule extends MockDaoModule {

	@Override
	protected void bindEntityGraphBuilder() {
		bind(IEntityGraphBuilder.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
	}

}
