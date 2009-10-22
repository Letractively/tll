/*
 * The Logic Lab
 */
package com.tll.di.test;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.IEntityFactory;
import com.tll.model.test.EntityBeanFactory;
import com.tll.model.test.EntityGraph;
import com.tll.model.test.IEntityGraphPopulator;

/**
 * EGraphModule - Provides {@link EntityGraph} instances.
 * @author jpk
 */
public abstract class EGraphModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(EGraphModule.class);

	protected abstract Class<? extends IEntityGraphPopulator> getEntityGraphBuilderImplType();

	/**
	 * @return The location of the Spring xml file containing the desired entity
	 *         declarations.
	 */
	protected abstract URI getBeanDefRef();

	@Override
	protected void configure() {
		log.info("Employing entity graph module.");

		bind(EntityBeanFactory.class).toProvider(new Provider<EntityBeanFactory>() {

			@Inject
			IEntityFactory efactory;

			@Override
			public EntityBeanFactory get() {
				final URI uri = getBeanDefRef();
				final ListableBeanFactory lbf = EntityBeanFactory.loadBeanDefinitions(uri);
				return new EntityBeanFactory(lbf, efactory);
			}
		}).in(Scopes.SINGLETON);

		// IEntityGraphPopulator
		bind(IEntityGraphPopulator.class).to(getEntityGraphBuilderImplType()).in(Scopes.SINGLETON);

		// EntityGraph
		bind(EntityGraph.class).toProvider(new Provider<EntityGraph>() {

			@Inject
			IEntityGraphPopulator builder;

			@Override
			public EntityGraph get() {
				final EntityGraph graph = new EntityGraph();
				builder.setEntityGraph(graph);
				// builder.populateEntityGraph();
				return graph;
			}
		}).asEagerSingleton();

	}

}
