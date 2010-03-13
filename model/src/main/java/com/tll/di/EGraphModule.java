/*
 * The Logic Lab
 */
package com.tll.di;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.IEntityFactory;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.model.egraph.EntityGraph;
import com.tll.model.egraph.IEntityGraphPopulator;
import com.tll.util.ClassUtil;

/**
 * EGraphModule - Provides {@link EntityGraph} instances.
 * @author jpk
 */
public class EGraphModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(EGraphModule.class);

	private static final String DEFAULT_BEANDEF_FILENAME = "com/tll/model/test/mock-entities.xml";

	/**
	 * The {@link IEntityGraphPopulator} impl type.
	 */
	protected final Class<? extends IEntityGraphPopulator> entityGraphPopulatorImplType;

	/**
	 * The location of the Spring xml file containing the desired entity
	 * declarations.
	 */
	protected final URI beanDefRef;

	/**
	 * Constructor - Relies on a default location for the xml bean def file
	 * @param entityGraphPopulatorImplType Required
	 */
	public EGraphModule(Class<? extends IEntityGraphPopulator> entityGraphPopulatorImplType) {
		this(entityGraphPopulatorImplType, null);
	}

	/**
	 * Constructor
	 * @param entityGraphPopulatorImplType Required
	 * @param beanDefFilePath Xml bean definition file path
	 */
	public EGraphModule(Class<? extends IEntityGraphPopulator> entityGraphPopulatorImplType, String beanDefFilePath) {
		super();
		this.entityGraphPopulatorImplType = entityGraphPopulatorImplType;
		if(beanDefFilePath == null) {
			log.info("Defaulting to bean def file path: " + DEFAULT_BEANDEF_FILENAME);
			beanDefFilePath = DEFAULT_BEANDEF_FILENAME;
		}
		try {
			this.beanDefRef = ClassUtil.getResource(beanDefFilePath).toURI();
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected void configure() {
		log.info("Employing entity graph module..");

		bind(EntityBeanFactory.class).toProvider(new Provider<EntityBeanFactory>() {

			@Inject(optional = true)
			IEntityFactory<?> entityFactory;

			@Override
			public EntityBeanFactory get() {
				
				final ListableBeanFactory lbf = EntityBeanFactory.loadBeanDefinitions(beanDefRef);
				return new EntityBeanFactory(lbf, entityFactory);
			}
		}).in(Scopes.SINGLETON);

		// IEntityGraphPopulator
		bind(IEntityGraphPopulator.class).to(entityGraphPopulatorImplType).in(Scopes.SINGLETON);

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
