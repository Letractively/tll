/* Created on - Jul 13, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 */

package com.tll.server.rpc.listing;

import javax.servlet.http.HttpServletRequest;

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
	 * @param request
	 * @param listingName
	 * @return the corresponding hash key for the given listing name in the given
	 *         http session.
	 */
	private static String key(HttpServletRequest request, String listingName) {
		return Integer.toString(request.getSession(false).getId().hashCode() ^ listingName.hashCode()).intern();
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
	 * @param request
	 * @param listingName
	 * @return listing handler
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListingHandler<T> getHandler(HttpServletRequest request, String listingName) {
		final Element e = handlerCache().get(key(request, listingName));
		return e == null ? null : (ListingHandler) e.getObjectValue();
	}

	/**
	 * Caches the handler by table view name.
	 * @param <T>
	 * @param request
	 * @param listingName
	 * @param handler
	 * @return the cache key under which the handler is stored.
	 */
	public static <T> String storeHandler(HttpServletRequest request, String listingName, ListingHandler<T> handler) {
		final String key = key(request, listingName);
		handlerCache().put(new Element(key, handler));
		return key;
	}

	/**
	 * Clears the cached handler by table view name.
	 * @param <T>
	 * @param request
	 * @param listingName
	 * @return the cleared handler. May be <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListingHandler<T> clearHandler(HttpServletRequest request, String listingName) {
		final String key = key(request, listingName);
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
	 * @param request
	 * @param listingName name of the table view for which state is retrieved.
	 * @return the cached listing state or null if not found.
	 */
	public static ListingState getState(HttpServletRequest request, String listingName) {
		final Element e = stateCache().get(key(request, listingName));
		return e == null ? null : (ListingState) e.getObjectValue();
	}

	/**
	 * Caches the state of the table for the duration of the request session.
	 * @param request
	 * @param listingName
	 * @param state
	 * @return The cache key
	 */
	public static String storeState(HttpServletRequest request, String listingName, ListingState state) {
		final String key = key(request, listingName);
		stateCache().put(new Element(key, state));
		return key;
	}

	/**
	 * Clears the listing state cached under the given table view name by table
	 * view name.
	 * @param request
	 * @param listingName
	 * @return the cleared table mode state. May be <code>null</code>.
	 */
	public static ListingState clearState(HttpServletRequest request, String listingName) {
		final String key = key(request, listingName);
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
	 * @param request
	 * @param retainState
	 */
	public static void clearAll(HttpServletRequest request, boolean retainState) {
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
