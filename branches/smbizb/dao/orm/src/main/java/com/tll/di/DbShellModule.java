package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.dao.jdbc.DbShell;
import com.tll.dao.jdbc.DbShellBuilder;

/**
 * DbShellModule - Provides a {@link DbShell}.
 * @author jpk
 */
public class DbShellModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(DbShellModule.class);

	Config config;

	/**
	 * Constructor
	 */
	public DbShellModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public DbShellModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing db shell module.");
		bind(DbShell.class).toProvider(new Provider<DbShell>() {

			public DbShell get() {
				try {
					return DbShellBuilder.getDbShell(config);
				}
				catch(final Exception e) {
					throw new IllegalStateException("Unable to provide a db shell: " + e.getMessage(), e);
				}
			}

		}).in(Scopes.SINGLETON);
	}
}