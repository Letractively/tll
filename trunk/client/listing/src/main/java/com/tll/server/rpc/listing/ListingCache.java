/* Created on - Jul 13, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 */

package com.tll.server.rpc.listing;

import java.io.Serializable;

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

	private static final String LIST_HANDLER_CACHE_NAME = "ListHandlerCache";

	private static final String LISTING_STATE_CACHE_NAME = "ListingStateCache";

	/**
	 * Generates the hash that is http session and listing name dependent.
	 * @param sessionId the required user session id (normally gotten from the
	 *        http request instance)
	 * @param listingName
	 * @return the corresponding hash key for the given listing name in the given
	 *         http session.
	 */
	private static Integer key(String sessionId, String listingName) {
		return Integer.valueOf(sessionId.hashCode() + 37 * listingName.hashCode());
	}

	private static Cache handlerCache() {
		return CacheManager.getInstance().getCache(LIST_HANDLER_CACHE_NAME);
	}

	private static Cache stateCache() {
		return CacheManager.getInstance().getCache(LISTING_STATE_CACHE_NAME);
	}

	/**
	 * Retrieves the cached handler by table view name.
	 * @param <T>
	 * @param sessionId
	 * @param listingName
	 * @return listing handler
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListingHandler<T> getHandler(String sessionId, String listingName) {
		final Element e = handlerCache().get(key(sessionId, listingName));
		return e == null ? null : (ListingHandler) e.getObjectValue();
	}

	/**
	 * Caches the handler by table view name.
	 * @param <T>
	 * @param sessionId
	 * @param listingName
	 * @param handler
	 */
	public static <T> void storeHandler(String sessionId, String listingName, ListingHandler<T> handler) {
		handlerCache().put(new Element(key(sessionId, listingName), handler));
	}

	/**
	 * Clears the cached handler by table view name.
	 * @param <T>
	 * @param sessionId
	 * @param listingName
	 * @return the cleared handler. May be <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListingHandler<T> clearHandler(String sessionId, String listingName) {
		final Serializable key = key(sessionId, listingName);
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
	 * @param listingName name of the table view for which state is retrieved.
	 * @return the cached listing state or null if not found.
	 */
	public static ListingState getState(String sessionId, String listingName) {
		final Element e = stateCache().get(key(sessionId, listingName));
		return e == null ? null : (ListingState) e.getObjectValue();
	}

	/**
	 * Caches the state of the table for the duration of the request session.
	 * @param sessionId
	 * @param listingName
	 * @param state
	 */
	public static void storeState(String sessionId, String listingName, ListingState state) {
		stateCache().put(new Element(key(sessionId, listingName), state));
	}

	/**
	 * Clears the listing state cached under the given table view name by table
	 * view name.
	 * @param sessionId
	 * @param listingName
	 * @return the cleared table mode state. May be <code>null</code>.
	 */
	public static ListingState clearState(String sessionId, String listingName) {
		final Serializable key = key(sessionId, listingName);
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
