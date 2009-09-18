/**
 * The Logic Lab
 * @author jpk
 * @since Sep 18, 2009
 */
package com.tll.di;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;


/**
 * TestPersistenceUnitModelModule
 * @author jpk
 */
public class TestPersistenceUnitModelModule implements Module {

	private final Module[] mods = new Module[2];

	/**
	 * Constructor
	 */
	public TestPersistenceUnitModelModule() {
		super();
		mods[0] = new ModelModule() {

			@Override
			protected void bindEntityAssembler() {
				bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
			}
		};
		mods[1] = new EGraphModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphPopulator.class).to(TestPersistenceUnitEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		};
	}

	@Override
	public void configure(Binder binder) {
		for(final Module m : mods) {
			m.configure(binder);
		}
	}
}
