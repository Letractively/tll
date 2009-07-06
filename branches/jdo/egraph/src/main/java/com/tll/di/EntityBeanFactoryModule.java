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
import com.tll.model.EntityBeanFactory;
import com.tll.model.EntityBeanFactory.EntityBeanFactoryParam;

/**
 * EntityBeanFactoryModule - Provides {@link EntityBeanFactory} instance.
 * @author jpk
 */
public class EntityBeanFactoryModule extends AbstractModule {

	public static final String DEFAULT_FILENAME = "mock-entities.xml";

	private static final Log log = LogFactory.getLog(EntityBeanFactoryModule.class);

	/**
	 * The name of the file containing the entity definitions expected to be at
	 * the root of the classpath.
	 */
	private final String filename;

	/**
	 * Constructor
	 */
	public EntityBeanFactoryModule() {
		this(DEFAULT_FILENAME);
	}

	/**
	 * Constructor
	 * @param filename
	 */
	public EntityBeanFactoryModule(String filename) {
		super();
		if(filename == null) throw new IllegalArgumentException("Null filename");
		this.filename = filename;
	}

	@Override
	protected void configure() {
		log.info("Employing mock entity factory module.");
		bind(ListableBeanFactory.class).annotatedWith(EntityBeanFactoryParam.class).toProvider(
				new Provider<ListableBeanFactory>() {

					@SuppressWarnings("synthetic-access")
					@Override
					public ListableBeanFactory get() {
						return new ClassPathXmlApplicationContext(filename);
					}
				});
		bind(EntityBeanFactory.class).in(Scopes.SINGLETON);
	}

}
