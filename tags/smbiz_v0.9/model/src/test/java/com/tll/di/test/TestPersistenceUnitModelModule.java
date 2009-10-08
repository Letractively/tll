/**
 * The Logic Lab
 * @author jpk
 * @since Sep 18, 2009
 */
package com.tll.di.test;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tll.di.ModelModule;
import com.tll.model.IEntityAssembler;
import com.tll.model.test.IEntityGraphPopulator;
import com.tll.model.test.TestPersistenceUnitEntityAssembler;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;

/**
 * TestPersistenceUnitModelModule
 * @author jpk
 */
public class TestPersistenceUnitModelModule implements Module {

	private final Module[] mods = new Module[] {

		new ModelModule() {

			@Override
			protected Class<? extends IEntityAssembler> getEntityAssemblerImplType() {
				return TestPersistenceUnitEntityAssembler.class;
			}
		},

		new EGraphModule() {

			@Override
			protected URI getBeanDefRef() {
				try {
					return Thread.currentThread().getContextClassLoader().getResource("com/tll/model/test/mock-entities.xml")
					.toURI();
				}
				catch(final URISyntaxException e) {
					return null;
				}
			}

			@Override
			protected Class<? extends IEntityGraphPopulator> getEntityGraphBuilderImplType() {
				return TestPersistenceUnitEntityGraphBuilder.class;
			}
		},
	};

	@Override
	public final void configure(Binder binder) {
		for(final Module m : mods) {
			m.configure(binder);
		}
	}

}
