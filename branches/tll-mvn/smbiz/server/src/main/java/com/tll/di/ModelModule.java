/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.BusinessKeyFactory;
import com.tll.model.EntityAssembler;
import com.tll.model.EntityFactory;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.key.IBusinessKeyFactory;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.SchemaInfo;

/**
 * ModelModule
 * @author jpk
 */
public class ModelModule extends GModule {

	@Override
	protected void configure() {
		// IEntityFactory
		bind(IEntityFactory.class).to(EntityFactory.class).in(Scopes.SINGLETON);

		// IEntityAssembler
		bind(IEntityAssembler.class).to(EntityAssembler.class).in(Scopes.SINGLETON);

		// ISchemaInfo
		bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);

		// IBusinessKeyFactory
		bind(IBusinessKeyFactory.class).to(BusinessKeyFactory.class).in(Scopes.SINGLETON);
	}

}
