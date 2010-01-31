/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.di;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.google.inject.Provider;
import com.tll.config.Config;

/**
 * SmbizJdoDaoModule
 * @author jpk
 */
public class SmbizJdoDaoModule extends JdoRdbmsDaoModule {

	/**
	 * Constructor
	 */
	public SmbizJdoDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public SmbizJdoDaoModule(Config config) {
		super(config);
	}

	@Override
	protected void bindPmf() {
		// we depend on jdoconfig.xml
		bind(PersistenceManagerFactory.class).toProvider(new Provider<PersistenceManagerFactory>() {

			@Override
			public PersistenceManagerFactory get() {
				final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");
				return pmf;
			}
		}).asEagerSingleton();
	}

}
