/**
 * The Logic Lab
 * @author jpk
 * @since Sep 18, 2009
 */
package com.tll.dao.db4o.test;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.tll.dao.db4o.Db4oEntityFactory;
import com.tll.model.EGraphModule;
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityMetadata;
import com.tll.model.ISchemaInfo;
import com.tll.model.ModelModule;
import com.tll.model.SchemaInfo;
import com.tll.model.test.TestEntityFactory;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;
import com.tll.model.validate.ValidationModule;

/**
 * TestPersistenceUnitModule
 * @author jpk
 */
public final class TestPersistenceUnitModule implements Module {

	private final String beanDefFilePath;

	/**
	 * Constructor
	 * @param beanDefFilePath Xml bean definition file path. If <code>null</code>,
	 *        the default location is used.
	 *        <code>null</code>, {@link TestEntityFactory} will be used.
	 * @see EGraphModule for the default xml bean def file locatoin
	 */
	public TestPersistenceUnitModule(String beanDefFilePath) {
		super();
		this.beanDefFilePath = beanDefFilePath;
	}

	@Override
	public final void configure(Binder binder) {
		new ValidationModule().configure(binder);
		new ModelModule() {
			
			@Override
			protected Class<? extends ISchemaInfo> getSchemaInfoImplType() {
				return SchemaInfo.class;
			}
			
			@Override
			protected Class<? extends IEntityMetadata> getEntityMetadataImplType() {
				return EntityMetadata.class;
			}
		}.configure(binder);
		
		// IEntityFactory
		binder.bind(new TypeLiteral<IEntityFactory<?>>() {}).to(Db4oEntityFactory.class).in(Scopes.SINGLETON);
		
		new EGraphModule(TestPersistenceUnitEntityGraphBuilder.class, this.beanDefFilePath).configure(binder);
	}

}
