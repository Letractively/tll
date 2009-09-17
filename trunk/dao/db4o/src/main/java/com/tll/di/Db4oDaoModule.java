package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.ConfigScope;
import com.db4o.config.Configuration;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.dao.IEntityDao;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;

/**
 * MockDaoModule - MOCK dao impl module.
 * @author jpk
 */
public class Db4oDaoModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(Db4oDaoModule.class);

	public static final String DB4O_FILENAME = "db4o";

	@Override
	protected void configure() {
		log.info("Binding db4o dao module...");

		// IPrimaryKeyGenerator
		bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);

		// Configuration (db4o)
		bind(Configuration.class).toProvider(new Provider<Configuration>() {

			@Override
			public Configuration get() {
				final Configuration c = Db4o.newConfiguration();
				c.generateVersionNumbers(ConfigScope.GLOBALLY);
				c.updateDepth(3);
				return c;
			}

		});

		// ObjectContainer
		bind(ObjectContainer.class).toProvider(new Provider<ObjectContainer>() {

			@Inject
			Configuration c;

			@Override
			public ObjectContainer get() {
				return Db4o.openFile(c, DB4O_FILENAME);
			}
		}).in(Scopes.SINGLETON);

		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.db4o.Db4oEntityDao.class).in(Scopes.SINGLETON);
	}

}