package com.tll.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.dao.IEntityDao;
import com.tll.di.MockEntityFactoryModule.ConfigKeys;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.MockPrimaryKeyGenerator;

/**
 * MockDaoModule - MOCK dao impl module.
 * @author jpk
 */
public class MockDaoModule extends AbstractModule implements IConfigAware {

	static final Log log = LogFactory.getLog(MockDaoModule.class);

	/**
	 * SecondaryMailSender annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
		@BindingAnnotation
		public @interface EntityGraphType {
	}

	Config config;

	/**
	 * Constructor
	 */
	public MockDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public MockDaoModule(Config config) {
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

		// IEntityGraphBuilder
		final String egbcn = config.getString(ConfigKeys.ENTITY_GRAPH_BUILDER_CLASSNAME.getKey());
		if(egbcn == null) {
			throw new IllegalStateException("No entity graph builder class name specified in the configuration");
		}
		try {
			final Class<? extends IEntityGraphBuilder> clz = (Class<? extends IEntityGraphBuilder>) Class.forName(egbcn);
			bind(IEntityGraphBuilder.class).to(clz).in(Scopes.SINGLETON);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No entity graph builder found for name: " + egbcn);
		}

		// EntityGraph
		bind(EntityGraph.class).toProvider(new Provider<EntityGraph>() {

			@Inject
			IEntityGraphBuilder builder;

			@Override
			public EntityGraph get() {
				return builder.buildEntityGraph();
			}
		}).in(Scopes.SINGLETON);

		// IPrimaryKeyGenerator
		bind(IPrimaryKeyGenerator.class).to(MockPrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.mock.EntityDao.class).in(Scopes.SINGLETON);
	}

}