/**
 * The Logic Lab
 * @author jpk
 * @since Dec 31, 2010
 */
package com.tll;

import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.dao.db4o.SmbizDb4oDaoModule;
import com.tll.model.IEntityAssembler;
import com.tll.model.SmbizEGraphModule;
import com.tll.model.SmbizEntityAssembler;
import com.tll.model.validate.ValidationModule;
import com.tll.service.entity.SmbizEntityServiceFactoryModule;
import com.tll.service.entity.SmbizEntityServiceFactoryModule.UserCacheAware;
import com.tll.util.ClassUtil;

/**
 * Combines multiple modules providing all needed for server side persistence a
 * la db4o.
 * @author jpk
 */
public class SmbizDb4oPersistModule implements Module, IConfigAware {
	
	static final Log log = LogFactory.getLog(SmbizDb4oPersistModule.class);

	private Config config;

	/**
	 * Constructor
	 * @param config
	 */
	public SmbizDb4oPersistModule(Config config) {
		super();
		this.config = config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}
	
	@Override
	public void configure(Binder binder) {
		log.info("Loading smbiz db4o persist related modules...");
		binder.install(new ValidationModule());
		binder.install(new SmbizEGraphModule());
		binder.install(new SmbizDb4oDaoModule(config));
		binder.bind(IEntityAssembler.class).to(SmbizEntityAssembler.class).in(Scopes.SINGLETON);
		// satisfy caching requirement for UserService
		binder.bind(CacheManager.class).annotatedWith(UserCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Override
			public CacheManager get() {
				return new CacheManager(ClassUtil.getResource("ehcache-smbiz-persist.xml"));
			}
		}).in(Scopes.SINGLETON);
		binder.install(new SmbizEntityServiceFactoryModule());
	}
}
