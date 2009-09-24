/**
 * The Logic Lab
 * @author jpk
 * @since Sep 18, 2009
 */
package com.tll.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.test.TestPersistenceUnitEntityAssembler;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;


/**
 * TestPersistenceUnitModelModule
 * @author jpk
 */
public class TestPersistenceUnitModelModule extends AbstractModule {

	@Override
	public void configure() {
		bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
		bind(IEntityGraphPopulator.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
	}
}
