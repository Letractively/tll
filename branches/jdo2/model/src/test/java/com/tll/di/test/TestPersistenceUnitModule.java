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
import com.google.inject.Scopes;
import com.tll.di.AbstractModelModule;
import com.tll.di.EGraphModule;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;
import com.tll.model.test.TestPersistenceUnitEntityAssembler;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;
import com.tll.util.ClassUtil;

/**
 * TestPersistenceUnitModule
 * @author jpk
 */
public class TestPersistenceUnitModule implements Module {

	private static final String DEFAULT_BEANDEF_FILENAME = "com/tll/model/test/mock-entities.xml";

	/**
	 * The default impl is {@link SimplePrimaryKeyGenerator}. Override as
	 * necessary.
	 * @return The {@link IPrimaryKeyGenerator} impl type.
	 */
	protected Class<? extends IPrimaryKeyGenerator> getPrimaryKeyGeneratorImplType() {
		return SimplePrimaryKeyGenerator.class;
	}

	/**
	 * @return The desired filename of the classpath resource holding the Spring
	 *         bean factory entity definitions.
	 */
	protected String getEGraphBeanDefRef() {
		return DEFAULT_BEANDEF_FILENAME;
	}

	private final Module[] mods = new Module[] {

		// IPrimaryKeyGenerator
		new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IPrimaryKeyGenerator.class).to(getPrimaryKeyGeneratorImplType()).in(Scopes.SINGLETON);
			}
		},

		new AbstractModelModule() {

			@Override
			protected Class<? extends IEntityAssembler> getEntityAssemblerImplType() {
				return TestPersistenceUnitEntityAssembler.class;
			}
		},

		new EGraphModule() {

			@Override
			protected URI getBeanDefRef() {
				try {
					final String cr = getEGraphBeanDefRef();
					return ClassUtil.getResource(cr).toURI();
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
