/**
 * The Logic Lab
 * @author jpk
 * Mar 31, 2009
 */
package com.tll;

import com.tll.config.Config;
import com.tll.dao.jdbc.DbShell;

/**
 * AbstractDbAwareTest - Provides extended classes with a {@link DbTestSupport}
 * ref.
 * @author jpk
 */
public abstract class AbstractDbAwareTest extends AbstractInjectedTest {

	private Config config;
	private DbTestSupport dbSupport;

	/**
	 * @return the config
	 */
	protected final Config getConfig() {
		if(config == null) {
			config = Config.load();
		}
		return config;
	}

	/**
	 * @return The db support helper object that provides a {@link DbShell} and
	 *         transaction handling.
	 */
	protected final DbTestSupport getDbSupport() {
		if(dbSupport == null) {
			dbSupport = new DbTestSupport(getConfig());
		}
		return dbSupport;
	}
}
