/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.di.test;

import javax.jdo.PersistenceManagerFactory;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.dao.IDbTrans;
import com.tll.dao.jdo.test.JdoTrans;
import com.tll.di.JdoDaoModule;
import com.tll.di.JdoRdbmsIdGeneratorModule;

/**
 * TestJdoDaoModule - Binds the modules: {@link JdoDaoModule} and
 * {@link JdoRdbmsIdGeneratorModule}.
 * @author jpk
 */
public class TestJdoDaoModule implements Module, IConfigAware {

	private Config config;

	/**
	 * Constructor
	 */
	public TestJdoDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public TestJdoDaoModule(Config config) {
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void configure(Binder binder) {

		new JdoDaoModule(config).configure(binder);

		new JdoRdbmsIdGeneratorModule().configure(binder);

		(new Module() {

			@Override
			public void configure(Binder theBinder) {
				theBinder.bind(IDbTrans.class).toProvider(new Provider<IDbTrans>() {

					@Inject
					PersistenceManagerFactory pmf;

					@Override
					public IDbTrans get() {
						return new JdoTrans(pmf);
					}
				}).in(Scopes.SINGLETON);
			}
		}).configure(binder);
	}

}
