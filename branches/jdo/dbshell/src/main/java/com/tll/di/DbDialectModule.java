/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.db.DbDialectHandlerBuilder;
import com.tll.db.IDbDialectHandler;

/**
 * DbDialectModule - Resolves the db "type" from the configuration and wires it
 * up accordingly.
 * <p>
 * The db dialect is used by the dao and db shell layers.
 * @author jpk
 */
public class DbDialectModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(DbDialectModule.class);

	Config config;

	/**
	 * Constructor
	 */
	public DbDialectModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public DbDialectModule(Config config) {
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
		log.info("Employing db dialect module.");
		bind(IDbDialectHandler.class).toProvider(new Provider<IDbDialectHandler>() {

			@Override
			public IDbDialectHandler get() {
				try {
					return DbDialectHandlerBuilder.getDbDialectHandler(config);
				}
				catch(final Exception e) {
					throw new IllegalStateException(e);
				}
			}
		}).in(Scopes.SINGLETON);

	}

}
