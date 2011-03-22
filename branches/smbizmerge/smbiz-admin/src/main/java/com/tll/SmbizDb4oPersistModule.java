/**
 * The Logic Lab
 * @author jpk
 * @since Dec 31, 2010
 */
package com.tll;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.dao.db4o.SmbizDb4oDaoModule;
import com.tll.model.EMF;
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
public class SmbizDb4oPersistModule extends AbstractModule {
	
	static final Logger log = LoggerFactory.getLogger(SmbizDb4oPersistModule.class);

	private final Config config;

	/**
	 * Constructor
	 * @param config
	 */
	public SmbizDb4oPersistModule(Config config) {
		super();
		this.config = config;
	}

	@Override
	public void configure() {
		log.info("Loading smbiz db4o persist related modules...");
		install(new ValidationModule());
		install(new SmbizDb4oDaoModule(config));
		
		bind(IEntityAssembler.class).to(SmbizEntityAssembler.class).in(Scopes.SINGLETON);
		
		bind(CacheManager.class).annotatedWith(UserCacheAware.class).toProvider(new Provider<CacheManager>() {

			@Override
			public CacheManager get() {
				return new CacheManager(ClassUtil.getResource("ehcache-smbiz-persist.xml"));
			}
		}).in(Scopes.SINGLETON);
		
		install(new SmbizEntityServiceFactoryModule());
		
		// required for satisfying GWT's RequestFactory (entity-RPC) feature
		requestStaticInjection(EMF.class);
		
		// dev/debug mode only:
		// TODO turn into config flag or something as we don't want this to boot in production mode!
		install(new SmbizEGraphModule());		
	}
}
