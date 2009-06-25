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
import com.tll.dao.IEntityDao;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.MockPrimaryKeyGenerator;

/**
 * MockDaoModule - MOCK dao impl module.
 * @author jpk
 */
public abstract class MockDaoModule extends AbstractModule {

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

	/**
	 * Responsible for binding an {@link IEntityGraphBuilder} implementation.
	 */
	protected abstract void bindEntityGraphBuilder();

	@Override
	protected void configure() {
		// IEntityGraphBuilder
		bindEntityGraphBuilder();

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