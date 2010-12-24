/**
 * The Logic Lab
 * @author jpk
 * @since Sep 18, 2009
 */
package com.tll.model.test;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tll.model.EGraphModule;
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityMetadata;
import com.tll.model.ISchemaInfo;
import com.tll.model.ModelBuildModule;
import com.tll.model.ModelModule;
import com.tll.model.SchemaInfo;
import com.tll.model.validate.ValidationModule;

/**
 * TestPersistenceUnitModule
 * @author jpk
 */
public final class TestPersistenceUnitModule implements Module {

	private final String beanDefFilePath;

	private final Class<? extends IEntityFactory<?>> entityFactoryImplType;

	/**
	 * Constructor
	 * @param beanDefFilePath Xml bean definition file path. If <code>null</code>,
	 *        the default location is used.
	 * @param entityFactoryImplType The {@link IEntityFactory} impl type. If
	 *        <code>null</code>, {@link TestEntityFactory} will be used.
	 * @see EGraphModule for the default xml bean def file locatoin
	 */
	public TestPersistenceUnitModule(String beanDefFilePath, Class<? extends IEntityFactory<?>> entityFactoryImplType) {
		super();
		this.beanDefFilePath = beanDefFilePath;
		this.entityFactoryImplType = entityFactoryImplType == null ? TestEntityFactory.class : entityFactoryImplType;
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
		new ModelBuildModule(entityFactoryImplType, TestPersistenceUnitEntityAssembler.class).configure(binder);
		new EGraphModule(TestPersistenceUnitEntityGraphBuilder.class, this.beanDefFilePath).configure(binder);
	}

}
