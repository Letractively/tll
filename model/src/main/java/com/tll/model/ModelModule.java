/**
 * The Logic Lab
 * @author jpk
 * @since Mar 16, 2010
 */
package com.tll.model;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.model.IEntityMetadata;


/**
 * Binds {@link IEntityMetadata} and {@link ISchemaInfo} impl types.
 * @author jpk
 */
public abstract class ModelModule extends AbstractModule {
	
	/**
	 * @return {@link IEntityMetadata} impl type.
	 */
	protected abstract Class<? extends IEntityMetadata> getEntityMetadataImplType();
	
	/**
	 * @return {@link ISchemaInfo} impl type.
	 */
	protected abstract Class<? extends ISchemaInfo> getSchemaInfoImplType();

	@Override
	protected void configure() {
		bind(IEntityMetadata.class).to(getEntityMetadataImplType()).in(Scopes.SINGLETON);
		bind(ISchemaInfo.class).to(getSchemaInfoImplType()).in(Scopes.SINGLETON);
	}

}
