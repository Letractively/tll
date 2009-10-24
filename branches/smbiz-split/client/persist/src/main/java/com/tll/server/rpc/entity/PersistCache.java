/**
 * The Logic Lab
 * @author jpk
 * @since Oct 23, 2009
 */
package com.tll.server.rpc.entity;

import net.sf.ehcache.CacheManager;

import com.google.inject.Inject;

/**
 * PersistCache - Generic way to encapsulate a caching mechanism for the
 * persist module.
 * @author jpk
 */
public class PersistCache {

	private final CacheManager cacheManager;

	/**
	 * Constructor
	 * @param cacheManager a {@link CacheManager} instance supporting persist
	 *        related cache regions
	 */
	@Inject
	public PersistCache(CacheManager cacheManager) {
		super();
		this.cacheManager = cacheManager;
	}

	/**
	 * Generalized hook method to gracefully shutdown the cache.
	 */
	public void shutdown() {
		cacheManager.shutdown();
	}
}
