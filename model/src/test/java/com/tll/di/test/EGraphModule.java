/*
 * The Logic Lab
 */
package com.tll.di.test;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.model.test.EntityBeanFactory;
import com.tll.model.test.EntityGraph;
import com.tll.model.test.IEntityGraphPopulator;
import com.tll.model.test.EntityBeanFactory.EntityBeanFactoryParam;

/**
 * EGraphModule - Provides {@link EntityGraph} instances.
 * @author jpk
 */
public abstract class EGraphModule extends AbstractModule {

	public static final String DEFAULT_FILENAME = "mock-entities.xml";

	private static final Log log = LogFactory.getLog(EGraphModule.class);

	/**
	 * Constructor
	 */
	public EGraphModule() {
		super();
	}

	protected abstract Class<? extends IEntityGraphPopulator> getEntityGraphBuilderImplType();

	/**
	 * @return The location of the Spring xml file containing the core entity
	 *         definitions.
	 */
	protected abstract URI getBeanDefRef();

	@Override
	protected void configure() {
		log.info("Employing entity graph module.");
		bind(ListableBeanFactory.class).annotatedWith(EntityBeanFactoryParam.class).toProvider(
				new Provider<ListableBeanFactory>() {

					@Override
					public ListableBeanFactory get() {
						URI beanDefRef = getBeanDefRef();
						if(beanDefRef == null) try {
							beanDefRef = new URI("mock-entities");
						}
						catch(final URISyntaxException e1) {
							throw new IllegalStateException("Can't locate entity bean def file");
						}

						// NOTE: we revert to the system class loader as opposed to Spring's
						// default current thread context class loader
						// so gmaven invoked groovy scripts don't blow up
						try {
							final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(new String[] {
								beanDefRef.toString()
							}, false);
							ac.setClassLoader(getClass().getClassLoader());
							ac.refresh();
							return ac;
						}
						catch(final BeanDefinitionStoreException e) {
							// presume the file could't be found at root of classpath
							// so fallback on a file-based class loader
							final FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext(new String[] {
								beanDefRef.toString()
							}, false);
							ac.setClassLoader(getClass().getClassLoader());
							ac.refresh();
							return ac;
						}
					}
				});

		bind(EntityBeanFactory.class).in(Scopes.SINGLETON);

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
