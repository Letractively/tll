/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.INamedQueryResolver;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.PersistContext;
import com.tll.server.rpc.entity.PersistServiceDelegate;


/**
 * ClientPersistModule
 * @author jpk
 */
public class ClientPersistModule extends AbstractModule implements IConfigAware {

	private static final Log log = LogFactory.getLog(ClientPersistModule.class);

	/**
	 * ConfigKeys - Configuration property keys for the app context.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		ENTITY_TYPE_RESOLVER_CLASSNAME("server.entityTypeResolver.classname"),
		PERSIST_SERVICE_IMPL_RESOLVER_CLASSNAME("server.persistServiceImplResolver.classname"),
		NAMED_QUERY_RESOLVER_CLASSNAME("server.namedQueryResolver.classname");

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
	public ClientPersistModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public ClientPersistModule(Config config) {
		super();
		setConfig(config);
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		if(config == null) throw new IllegalStateException("No config instance specified.");
		log.info("Employing Client Persistence");

		String cn;

		// IEntityTypeResolver
		cn = config.getString(ConfigKeys.ENTITY_TYPE_RESOLVER_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No entity type resolver class name specified in the configuration");
		}
		Class<? extends IEntityTypeResolver> etrClass;
		try {
			etrClass = (Class<? extends IEntityTypeResolver>) Class.forName(cn);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No entity type resolver found for name: " + cn);
		}
		bind(IEntityTypeResolver.class).to(etrClass).in(Scopes.SINGLETON);

		// IPersistServiceImplResolver
		cn = config.getString(ConfigKeys.PERSIST_SERVICE_IMPL_RESOLVER_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No entity type resolver class name specified in the configuration");
		}
		Class<? extends IPersistServiceImplResolver> psiClass;
		try {
			psiClass = (Class<? extends IPersistServiceImplResolver>) Class.forName(cn);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No persist service impl found for name: " + cn);
		}
		bind(IPersistServiceImplResolver.class).to(psiClass).in(Scopes.SINGLETON);

		// INamedQueryResolver
		cn = config.getString(ConfigKeys.NAMED_QUERY_RESOLVER_CLASSNAME.getKey());
		if(cn == null) {
			throw new IllegalStateException("No entity type resolver class name specified in the configuration");
		}
		Class<? extends INamedQueryResolver> nqClass;
		try {
			nqClass = (Class<? extends INamedQueryResolver>) Class.forName(cn);
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("No named query resolver found for name: " + cn);
		}
		bind(INamedQueryResolver.class).to(nqClass).in(Scopes.SINGLETON);

		// PersistContext
		bind(PersistContext.class).in(Scopes.SINGLETON);

		// PersistServiceDelegate
		bind(PersistServiceDelegate.class).in(Scopes.SINGLETON);
	}

}
