/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.model.EntityFactory;
import com.tll.model.IEntityFactory;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.SchemaInfo;

/**
 * ModelModule
 * @author jpk
 */
public class ModelModule extends AbstractModule {

	@Override
	protected void configure() {
		// IEntityFactory
		bind(IEntityFactory.class).to(EntityFactory.class).in(Scopes.SINGLETON);

		// ISchemaInfo
		bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);

		// NO. This is an entity service specific construct!
		// IEntityAssembler
		/*
		Class<IEntityAssembler> clz;
		final String cn = Config.instance().getString(ConfigKeys.MODEL_ENTITY_ASSEMBLER_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No entity assembler class name specified in the configuration");
		}
		try {
			clz = (Class<IEntityAssembler>) Class.forName(cn);
		}
		catch(ClassNotFoundException e) {
			throw new IllegalStateException("No entity assembler found for name: " + cn);
		}
		bind(IEntityAssembler.class).to(clz).in(Scopes.SINGLETON);
		*/
	}

}
