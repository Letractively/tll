/*
 * The Logic Lab 
 */
package com.tll.di;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.model.MockEntityProvider.MockEntityBeanFactory;

/**
 * MockEntitiesModule
 * @author jpk
 */
public class MockEntitiesModule extends GModule {

	/**
	 * ConfigKeys - Defines the necessary config keys for this module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		MOCK_ENTITIES_FILENAME("mock.entities.filename");

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
		final ClassPathXmlApplicationContext sac = new ClassPathXmlApplicationContext(mef/*"mock-entities.xml"*/);
		bind(ListableBeanFactory.class).annotatedWith(MockEntityBeanFactory.class).toInstance(sac);
		// bind(IEntityProvider.class).to(EntityGraph.class).in(Scopes.SINGLETON);
	}

}
