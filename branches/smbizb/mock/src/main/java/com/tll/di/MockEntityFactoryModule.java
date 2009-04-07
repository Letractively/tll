/*
 * The Logic Lab
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.model.MockEntityFactory;
import com.tll.model.MockEntityFactory.MockEntityBeanFactory;

/**
 * MockEntityFactoryModule - Use for providing a {@link MockEntityFactory}
 * instance based on Spring compatable bean xml definition file.
 * @author jpk
 */
public class MockEntityFactoryModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(MockEntityFactoryModule.class);

	/**
	 * ConfigKeys - Defines the necessary config keys for this module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		/**
		 * The xml file containing the entity prototype definitions.
		 */
		MOCK_ENTITIES_FILENAME("model.mockEntities.filename"),

		/**
		 * The complimenting entity object graph builder.
		 */
		ENTITY_GRAPH_BUILDER_CLASSNAME("model.entityGraphBuilder.classname");

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
	public MockEntityFactoryModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public MockEntityFactoryModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing mock entity factory module.");
		bind(ListableBeanFactory.class).annotatedWith(MockEntityBeanFactory.class).toProvider(
				new Provider<ListableBeanFactory>() {

					@Override
					public ListableBeanFactory get() {
						final String mef = config.getString(ConfigKeys.MOCK_ENTITIES_FILENAME.getKey());
						return new ClassPathXmlApplicationContext(mef);
					}
				});
		bind(MockEntityFactory.class).in(Scopes.SINGLETON);
	}

}
