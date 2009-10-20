/* Created on - Jul 13, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 */

package com.tll.server.rpc.listing;

import java.io.Serializable;
import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Dedicated cache implementation for table view processing. Stores, retrieves
 * and clears listing related state in the HTTP request session
 * <em>independent</em> of the servlet path.
 * @author jpk
 */
public final class ListingCache {

	private static final String SMBIZ_WEB_CACHEMANAGER_FILENAME = "ehcache-smbiz-web.xml";

	private static final String LIST_HANDLER_CACHE_NAME = "ListHandlerCache";

	private static final String LISTING_STATE_CACHE_NAME = "ListingStateCache";

	private static final ThreadLocal<CacheManager> tlCm;

	static {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(SMBIZ_WEB_CACHEMANAGER_FILENAME);
		final CacheManager cm = new CacheManager(url);
		tlCm = new ThreadLocal<CacheManager>();
		tlCm.set(cm);
	}

	private static CacheManager getCacheManager() {
		return tlCm.get();
	}

	/**
	 * Generates the hash that is http session and listing name dependent.
	 * @param sessionId the required user session id (normally gotten from the
	 *        http request instance)
	 * @param listingId
	 * @return the corresponding hash key for the given listing name in the given
	 *         http session.
	 */
	private static Integer key(String sessionId, String listingId) {
		return Integer.valueOf(sessionId.hashCode() + 37 * listingId.hashCode());
	}

	private static Cache handlerCache() {
		return getCacheManager().getCache(LIST_HANDLER_CACHE_NAME);
	}

	private static Cache stateCache() {
		return getCacheManager().getCache(LISTING_STATE_CACHE_NAME);
	}

	/**
	 * Retrieves the cached handler by table view name.
	 * @param <T>
	 * @param sessionId
	 * @param listingId
	 * @return listing handler
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListingHandler<T> getHandler(String sessionId, String listingId) {
		final Element e = handlerCache().get(key(sessionId, listingId));
		return e == null ? null : (ListingHandler) e.getObjectValue();
	}

	/**
	 * Caches the handler by table view name.
	 * @param <T>
	 * @param sessionId
	 * @param listingId
	 * @param handler
	 */
	public static <T> void storeHandler(String sessionId, String listingId, ListingHandler<T> handler) {
		handlerCache().put(new Element(key(sessionId, listingId), handler));
	}

	/**
	 * Clears the cached handler by table view name.
	 * @param <T>
	 * @param sessionId
	 * @param listingId
	 * @return the cleared handler. May be <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListingHandler<T> clearHandler(String sessionId, String listingId) {
		final Serializable key = key(sessionId, listingId);
		final Cache c = handlerCache();
		final Element e = c.get(key);
		final ListingHandler<T> handler = e == null ? null : (ListingHandler<T>) e.getObjectValue();
		if(handler != null) {
			c.remove(key);
		}
		return handler;
	}

	/**
	 * Retrieves the cached listing state by table view name.
	 * @param sessionId
	 * @param listingId name of the table view for which state is retrieved.
	 * @return the cached listing state or null if not found.
	 */
	public static ListingState getState(String sessionId, String listingId) {
		final Element e = stateCache().get(key(sessionId, listingId));
		return e == null ? null : (ListingState) e.getObjectValue();
	}

	/**
	 * Caches the state of the table for the duration of the request session.
	 * @param sessionId
	 * @param listingId
	 * @param state
	 */
	public static void storeState(String sessionId, String listingId, ListingState state) {
		stateCache().put(new Element(key(sessionId, listingId), state));
	}

	/**
	 * Clears the listing state cached under the given table view name by table
	 * view name.
	 * @param sessionId
	 * @param listingId
	 * @return the cleared table mode state. May be <code>null</code>.
	 */
	public static ListingState clearState(String sessionId, String listingId) {
		final Serializable key = key(sessionId, listingId);
		final Cache c = stateCache();
		final Element e = c.get(key);
		final ListingState state = e == null ? null : (ListingState) e.getObjectValue();
		if(state != null) {
			c.remove(key);
		}
		return state;
	}

	/**
	 * Clears out all listing cachings with the option to retian listing state
	 * caches.
	 * @param retainState
	 */
	public static void clearAll(boolean retainState) {
		handlerCache().removeAll();
		if(!retainState) {
			stateCache().removeAll();
		}
	}

	/**
	 * Constructor
	 */
	private ListingCache() {
	}
}
