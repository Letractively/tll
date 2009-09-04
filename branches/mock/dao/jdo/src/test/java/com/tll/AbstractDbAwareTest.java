/**
 * The Logic Lab
 * @author jpk
 * Mar 31, 2009
 */
package com.tll;

import com.tll.config.Config;
import com.tll.dao.IDbShell;

/**
 * AbstractDbAwareTest - Provides {@link IDbShell} and db trans support to test
 * classes.
 * @author jpk
 */
public abstract class AbstractDbAwareTest extends AbstractInjectedTest {

	private Config config;
	private DbTestSupport dbSupport;
	private IDbShell dbShell;

	/**
	 * @return the config instance.
	 */
	protected final Config getConfig() {
		if(config == null) {
			config = Config.load();
		}
		return config;
	}

	/**
	 * @return The {@link DbTestSupport} instance.
	 */
	protected final DbTestSupport getDbSupport() {
		if(dbSupport == null) {
			dbSupport = injector.getInstance(DbTestSupport.class);
		}
		return dbSupport;
	}

	protected final IDbShell getDbShell() {
		if(dbShell == null) {
			dbShell = injector.getInstance(IDbShell.class);
		}
		return dbShell;
	}
}
