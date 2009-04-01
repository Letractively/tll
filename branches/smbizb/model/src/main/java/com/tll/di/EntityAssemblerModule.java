/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.IEntityAssembler;


/**
 * EntityAssemblerModule
 * @author jpk
 */
public class EntityAssemblerModule extends GModule {

	/**
	 * ConfigKeys - Configuration property keys for the model module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		ENTITY_ASSEMBLER_CLASSNAME("model.entityAssembler.classname");

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
		
		// IEntityAssembler
		Class<IEntityAssembler> clz;
		final String cn = Config.instance().getString(ConfigKeys.ENTITY_ASSEMBLER_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No entity assembler class name specified in the configuration");
		}
		try {
			clz = (Class<IEntityAssembler>) Class.forName(cn);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No entity assembler found for name: " + cn);
		}
		bind(IEntityAssembler.class).to(clz).in(Scopes.SINGLETON);
	}

}
