package com.tll.dao.db4o.test;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.dao.IDbShell;
import com.tll.dao.db4o.AbstractDb4oDaoModule.Db4oFile;
import com.tll.dao.db4o.Db4oDbShell;
import com.tll.model.egraph.IEntityGraphPopulator;

/**
 * Db4oDbShellModule - Db4o db shell impl module.
 * @author jpk
 */
public class Db4oDbShellModule extends AbstractModule {

	static final Logger log = LoggerFactory.getLogger(Db4oDbShellModule.class);

	@Override
	protected void configure() {
		log.info("Loading db4o db shell module...");

		// IDbShell
		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@Inject
			@Db4oFile
			URI db4oUri;

			@Inject(optional = true)
			Provider<EmbeddedConfiguration> c;

			@Inject(optional = true)
			IEntityGraphPopulator populator;

			@Override
			public IDbShell get() {
				return new Db4oDbShell(db4oUri, populator, c);
			}

		}).in(Scopes.SINGLETON);
	}

}
