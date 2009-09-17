/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.di;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.config.Configuration;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.dao.IDbShell;
import com.tll.dao.db4o.Db4oDbShell;
import com.tll.model.IEntityGraphPopulator;

/**
 * MockDbShellModule - Depends on the {@link EGraphModule}.
 * @author jpk
 */
public class Db4oDbShellModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(Db4oDbShellModule.class);

	/**
	 * Constructor
	 */
	public Db4oDbShellModule() {
		super();
	}

	@Override
	protected void configure() {
		log.info("Employing db4o db shell module.");

		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@Inject(optional = true)
			Configuration c;

			@Inject
			IEntityGraphPopulator populator;

			@Override
			public IDbShell get() {
				final File f = new File(Db4oDaoModule.DB4O_FILENAME);
				return new Db4oDbShell(f.toURI(), populator, c);
			}

		}).in(Scopes.SINGLETON);
	}

}
