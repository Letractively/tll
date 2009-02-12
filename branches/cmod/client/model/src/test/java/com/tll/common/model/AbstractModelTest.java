/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import org.testng.annotations.BeforeClass;

import com.tll.AbstractInjectedTest;

/**
 * AbstractModelTest - Base class for testing client model stuff.
 * @author jpk
 */
public abstract class AbstractModelTest extends AbstractInjectedTest {

	/*
	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				// IPrimaryKeyGenerator
				binder.bind(IPrimaryKeyGenerator.class).to(MockPrimaryKeyGenerator.class).in(Scopes.SINGLETON);
			}
		});
	}
	*/
	
	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	/*
	protected final MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}

	protected final Marshaler getMarshaler() {
		return injector.getInstance(Marshaler.class);
	}
	*/
}
