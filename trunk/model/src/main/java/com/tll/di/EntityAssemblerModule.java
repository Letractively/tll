/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.model.IEntityAssembler;


/**
 * EntityAssemblerModule
 * @author jpk
 */
public class EntityAssemblerModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(EntityAssemblerModule.class);

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

	Config config;

	/**
	 * Constructor
	 */
	public EntityAssemblerModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public EntityAssemblerModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing entity asssembler module.");
		// IEntityAssembler
		Class<IEntityAssembler> clz;
		final String cn = config.getString(ConfigKeys.ENTITY_ASSEMBLER_CLASSNAME.getKey());
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
