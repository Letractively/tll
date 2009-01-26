/**
 * The Logic Lab
 * @author jpk
 * Jan 19, 2009
 */
package com.tll.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.EntityFactory;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.SchemaInfo;

/**
 * ModelModule
 * @author jpk
 */
public class ModelModule extends AbstractModule {

	/**
	 * ConfigKeys - Configuration property keys for the model module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		MODEL_ENTITY_ASSEMBLER_CLASSNAME("model.entityAssembler.classname");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		// IEntityFactory
		bind(IEntityFactory.class).to(EntityFactory.class).in(Scopes.SINGLETON);

		// ISchemaInfo
		bind(ISchemaInfo.class).to(SchemaInfo.class).in(Scopes.SINGLETON);

		// IEntityAssembler
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
	}

}
