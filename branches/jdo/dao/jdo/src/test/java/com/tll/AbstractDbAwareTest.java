/**
 * The Logic Lab
 * @author jpk
 * Mar 31, 2009
 */
package com.tll;

import javax.jdo.PersistenceManager;

import com.tll.config.Config;

/**
 * AbstractDbAwareTest - Provides extended classes with a {@link DbTestSupport}
 * ref.
 * @author jpk
 */
public abstract class AbstractDbAwareTest extends AbstractInjectedTest {

	private Config config;
	private final PersistenceManager pm;
	private DbTestSupport dbSupport;

	/**
	 * Constructor
	 * @param pm
	 */
	public AbstractDbAwareTest(PersistenceManager pm) {
		super();
		this.pm = pm;
	}

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
			dbSupport = new DbTestSupport(getConfig(), pm);
		}
		return dbSupport;
	}
}
