/* Created on - Jul 13, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 */

package com.tll.server.rpc.listing;

import javax.servlet.http.HttpServletRequest;

import com.tll.server.cache.WebCache;

/**
 * Dedicated cache implementation for table view processing. Stores, retrieves
 * and clears listing related state in the HTTP request session
 * <em>independent</em> of the servlet path.
 * @author jpk
 */
public class ListingCache {

	/**
	 * Retrieves the cached handler by table view name.
	 * @param request
	 * @param listingName
	 */
	@SuppressWarnings("unchecked")
	public static IListingHandler getHandler(HttpServletRequest request, String listingName) {
		return (IListingHandler) WebCache.retrieveWithCacheKeys(request, listingName, IListingHandler.class);
	}

	/**
	 * Caches the handler by table view name.
	 * @param request
	 * @param listingName
	 * @param handler
	 * @return the cache key under which the handler is stored.
	 */
	@SuppressWarnings("unchecked")
	public static String storeHandler(HttpServletRequest request, String listingName, IListingHandler handler) {
		return WebCache.storePageScopeWithCacheKeys(request, listingName, IListingHandler.class, handler);
	}

	/**
	 * Clears the cached handler by table view name.
	 * @param request
	 * @param listingName
	 * @return the cleared handler. May be <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static IListingHandler clearHandler(HttpServletRequest request, String listingName) {
		return (IListingHandler) WebCache.clearWithCacheKeys(request, listingName, IListingHandler.class);
	}

	/**
	 * Retrieves the cached listing state by table view name.
	 * @param request
	 * @param listingName name of the table view for which state is retrieved.
	 * @return the cached listing state or null if not found.
	 */
	public static ListingState getState(HttpServletRequest request, String listingName) {
		return (ListingState) WebCache.retrieveWithCacheKeys(request, listingName, ListingState.class);
	}

	/**
	 * Caches the state of the table for the duration of the request session.
	 * @param request
	 * @param listingName
	 * @return The cache key
	 */
	public static String storeState(HttpServletRequest request, String listingName, ListingState state) {
		return WebCache.storeSessionScopeWithCacheKeys(request, listingName, ListingState.class, state);
	}

	/**
	 * Clears the listing state cached under the given table view name by table
	 * view name.
	 * @param request
	 * @param listingName
	 * @return the cleared table mode state. May be <code>null</code>.
	 */
	public static ListingState clearState(HttpServletRequest request, String listingName) {
		return (ListingState) WebCache.clearWithCacheKeys(request, listingName, ListingState.class);
	}

	/**
	 * Clears out all listing cachings with the option to retian listing state
	 * caches.
	 * @param request
	 * @param retainState
	 */
	public static void clearAll(HttpServletRequest request, boolean retainState) {
		WebCache.clearAllCacheKeysHavingCacheClass(request, IListingHandler.class);
		if(!retainState) {
			WebCache.clearAllCacheKeysHavingCacheClass(request, ListingState.class);
		}
	}

	/**
	 * Constructor
	 */
	private ListingCache() {
	}
}
