/*
 * The Logic Lab 
 */
package com.tll.di;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.MockEntityFactory;
import com.tll.model.MockEntityFactory.MockEntityBeanFactory;

/**
 * MockEntityFactoryModule - Use for providing a {@link MockEntityFactory}
 * instance based on Spring compatable bean xml definition file.
 * @author jpk
 */
public class MockEntityFactoryModule extends GModule {

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
	
	@Override
	protected void configure() {
		final String mef = Config.instance().getString(ConfigKeys.MOCK_ENTITIES_FILENAME.getKey());
		final ClassPathXmlApplicationContext sac = new ClassPathXmlApplicationContext(mef);
		bind(ListableBeanFactory.class).annotatedWith(MockEntityBeanFactory.class).toInstance(sac);
		bind(MockEntityFactory.class).in(Scopes.SINGLETON);
	}

}
