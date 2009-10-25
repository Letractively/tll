/**
 * The Logic Lab
 * @author jpk
 * @since Oct 23, 2009
 */
package com.tll.server.rpc.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.ehcache.CacheManager;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;

/**
 * PersistCache - Generic way to encapsulate a caching mechanism for the
 * persist module.
 * @author jpk
 */
public class PersistCache {

	/**
	 * PersistCacheAware<br>
	 * Annotation indicating a {@link CacheManager} instance that supports persist related caching.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
		@BindingAnnotation
		public @interface PersistCacheAware {
	}

	private final CacheManager cacheManager;

	/**
	 * Constructor
	 * @param cacheManager a {@link CacheManager} instance supporting persist
	 *        related cache regions
	 */
	@Inject
	public PersistCache(@PersistCacheAware CacheManager cacheManager) {
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
