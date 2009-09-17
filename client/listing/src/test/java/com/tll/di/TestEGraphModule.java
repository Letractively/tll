/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;


/**
 * TestEGraphModule
 * @author jpk
 */
public class TestEGraphModule extends EGraphModule {

	@Override
	protected void bindEntityGraphBuilder() {
		bind(IEntityGraphPopulator.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
	}

}
