/*
 * The Logic Lab
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.EntityBeanFactory;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.EntityBeanFactory.EntityBeanFactoryParam;

/**
 * EGraphModule - Provides {@link EntityGraph} instances.
 * @author jpk
 */
public abstract class EGraphModule extends AbstractModule {

	public static final String DEFAULT_FILENAME = "mock-entities.xml";

	private static final Log log = LogFactory.getLog(EGraphModule.class);

	/**
	 * The name of the file containing the entity definitions expected to be at
	 * the root of the classpath.
	 */
	private String filename = DEFAULT_FILENAME;

	/**
	 * Sets the name of the file containing the entity definitions expected to be
	 * at the root of the classpath.
	 * @param filename
	 */
	protected void setBeanDefFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Binds an {@link IEntityGraphPopulator} impl.
	 */
	protected abstract void bindEntityGraphBuilder();

	@Override
	protected void configure() {
		log.info("Employing entity graph module.");
		bind(ListableBeanFactory.class).annotatedWith(EntityBeanFactoryParam.class).toProvider(
				new Provider<ListableBeanFactory>() {

					@SuppressWarnings("synthetic-access")
					@Override
					public ListableBeanFactory get() {
						return new ClassPathXmlApplicationContext(filename);
					}
				});
		bind(EntityBeanFactory.class).in(Scopes.SINGLETON);

		// IEntityGraphPopulator
		bindEntityGraphBuilder();

		// EntityGraph
		bind(EntityGraph.class).toProvider(new Provider<EntityGraph>() {

			@Inject
			IEntityGraphPopulator builder;

			@Override
			public EntityGraph get() {
				final EntityGraph graph = new EntityGraph();
				builder.setEntityGraph(graph);
				//builder.populateEntityGraph();
				return graph;
			}
		}).asEagerSingleton();

	}

}
