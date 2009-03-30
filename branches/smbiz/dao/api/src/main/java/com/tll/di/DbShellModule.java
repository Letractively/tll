package com.tll.di;

import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.dao.jdbc.DbShell;
import com.tll.dao.jdbc.DbShellBuilder;

/**
 * DbShellModule
 * @author jpk
 */
public class DbShellModule extends GModule {

	@Override
	protected void configure() {

		bind(DbShell.class).toProvider(new Provider<DbShell>() {

			public DbShell get() {
				try {
					return DbShellBuilder.getDbShell(Config.instance());
				}
				catch(Exception e) {
					throw new IllegalStateException("Unable to provide a db shell: " + e.getMessage(), e);
				}
			}

		}).in(Scopes.SINGLETON);
	}
}