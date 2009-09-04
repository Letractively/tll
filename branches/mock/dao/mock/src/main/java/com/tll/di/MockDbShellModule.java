/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IDbShell;
import com.tll.dao.mock.MockDbShell;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphPopulator;

/**
 * MockDbShellModule
 * @author jpk
 */
public class MockDbShellModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(MockDbShellModule.class);

	/**
	 * ConfigKeys
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		EGRAPH_POPULATOR_CLASSNAME("egraph.populator.classname");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	Config config;

	/**
	 * Constructor
	 */
	public MockDbShellModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public MockDbShellModule(Config config) {
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
		log.info("Employing mock db shell module.");

		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {

			@SuppressWarnings("unchecked")
			@Override
			public IDbShell get() {
				final String cn = config.getString(ConfigKeys.EGRAPH_POPULATOR_CLASSNAME.getKey());
				Class<? extends IEntityGraphPopulator> clz;
				try {
					clz = (Class<? extends IEntityGraphPopulator>) Class.forName(cn);
				}
				catch(final ClassNotFoundException e1) {
					throw new IllegalStateException(e1);
				}
				IEntityGraphPopulator r;
				try {
					r = clz.newInstance();
				}
				catch(final InstantiationException e) {
					throw new IllegalStateException(e);
				}
				catch(final IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
				return new MockDbShell(new EntityGraph(), r);
			}

		}).in(Scopes.SINGLETON);
	}

}
