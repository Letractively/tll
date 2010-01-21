/**
 * The Logic Lab
 * @author jpk
 * @since Sep 18, 2009
 */
package com.tll.di.test;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tll.di.EGraphModule;
import com.tll.di.ModelBuildModule;
import com.tll.di.ModelModule;
import com.tll.model.key.SimplePrimaryKeyGenerator;
import com.tll.model.test.TestPersistenceUnitEntityAssembler;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;

/**
 * TestPersistenceUnitModule
 * @author jpk
 */
public final class TestPersistenceUnitModule implements Module {

	protected final String beanDefFilePath;

	/**
	 * Constructor - Defaults to employing:
	 * <ul>
	 * <li>Default xml bean def location
	 * <li>{@link SimplePrimaryKeyGenerator}
	 * </ul>
	 * @see TestPersistenceUnitModule#TestPersistenceUnitModule(String)
	 */
	public TestPersistenceUnitModule() {
		this(null);
	}

	/**
	 * Constructor
	 * @param beanDefFilePath Xml bean definition file path. If <code>null</code>,
	 *        the default location is used.
	 * @see EGraphModule for the default xml bean def file locatoin
	 */
	public TestPersistenceUnitModule(String beanDefFilePath) {
		super();
		this.beanDefFilePath = beanDefFilePath;
	}

	private final Module[] mods = new Module[] {

		new ModelModule(),

		new ModelBuildModule(TestPersistenceUnitEntityAssembler.class),

		new EGraphModule(TestPersistenceUnitEntityGraphBuilder.class, this.beanDefFilePath), };

	@Override
	public final void configure(Binder binder) {
		for(final Module m : mods) {
			m.configure(binder);
		}
	}

}
