/* Created on - Jul 20, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 */

package com.tll.server.cache;

import javax.servlet.http.HttpServletRequest;

import com.tll.server.Constants;
import com.tll.server.ServletUtil;

/**
 * Enables clients to utilize the {@link CacheManager}.
 * @author jpk
 */
public final class WebCache {

	private static CacheManager get(HttpServletRequest request) {
		return (CacheManager) ServletUtil.getSessionAttribute(request, Constants.SA_CACHE_MANAGER);
	}

	public static String getLastSynchronousPath(HttpServletRequest request) {
		return get(request).getLastSynchronousPath();
	}

	public static String getCacheKey(HttpServletRequest request, Class<?> cacheClass) {
		return get(request).getPageCacheKey(request, cacheClass);
	}

	public static String getCacheKey(HttpServletRequest request, String key, Class<?> cacheClass) {
		return get(request).getPageCacheKey(request, key, cacheClass);
	}

	public static String getCacheKey(HttpServletRequest request, String key) {
		return get(request).getPageCacheKey(request, key);
	}

	public static Object retrieve(HttpServletRequest request, Class<?> cacheClass) {
		return get(request).retrieve(request, cacheClass);
	}

	public static Object retrieve(HttpServletRequest request, String key) {
		return get(request).retrieve(request, key);
	}

	public static Object retrieve(HttpServletRequest request, String key, Class<?> cacheClass) {
		return get(request).retrieve(request, key, cacheClass);
	}

	public static Object retrieveWithCacheKey(HttpServletRequest request, String cacheKey) {
		return get(request).retrieveWithCacheKey(request, cacheKey);
	}

	public static Object retrieveWithCacheKeys(HttpServletRequest request, String cacheKey, Class<?> cacheClass) {
		return get(request).retrieveWithCacheKeys(cacheKey, cacheClass);
	}

	public static String storePageScope(HttpServletRequest request, String key, Object obj) {
		return get(request).storePageScope(request, key, obj);
	}

	public static String storePageScope(HttpServletRequest request, Class<?> cacheClass, Object obj) {
		return get(request).storePageScope(request, cacheClass, obj);
	}

	public static String storePageScope(HttpServletRequest request, String key, Class<?> cacheClass, Object obj) {
		return get(request).storePageScope(request, key, cacheClass, obj);
	}

	public static void storePageScopeWithCacheKey(HttpServletRequest request, String cacheKey, Object obj) {
		get(request).storePageScopeWithCacheKey(request, cacheKey, obj);
	}

	public static String storePageScopeWithCacheKeys(HttpServletRequest request, String cacheKey, Class<?> cacheClass,
			Object obj) {
		return get(request).storePageScopeWithCacheKeys(request, cacheKey, cacheClass, obj);
	}

	public static String storeSessionScope(HttpServletRequest request, String key, Object obj) {
		return get(request).storeSessionScope(request, key, obj);
	}

	public static String storeSessionScope(HttpServletRequest request, Class<?> cacheClass, Object obj) {
		return get(request).storeSessionScope(request, cacheClass, obj);
	}

	public static String storeSessionScope(HttpServletRequest request, String key, Class<?> cacheClass, Object obj) {
		return get(request).storeSessionScope(request, key, cacheClass, obj);
	}

	public static void storeSessionScopeWithCacheKey(HttpServletRequest request, String cacheKey, Object obj) {
		get(request).storeSessionScopeWithCacheKey(cacheKey, obj);
	}

	public static String storeSessionScopeWithCacheKeys(HttpServletRequest request, String cacheKey, Class<?> cacheClass,
			Object obj) {
		return get(request).storeSessionScopeWithCacheKeys(cacheKey, cacheClass, obj);
	}

	public static Object clear(HttpServletRequest request, String key) {
		return get(request).clear(request, key);
	}

	public static Object clear(HttpServletRequest request, Class<?> cacheClass) {
		return get(request).clear(request, cacheClass);
	}

	public static Object clear(HttpServletRequest request, String key, Class<?> cacheClass) {
		return get(request).clear(request, key, cacheClass);
	}

	public static Object clearWithCacheKey(HttpServletRequest request, String cacheKey) {
		return get(request).clearWithCacheKey(cacheKey);
	}

	public static Object clearWithCacheKeys(HttpServletRequest request, String cacheKey, Class<?> cacheClass) {
		return get(request).clearWithCacheKeys(cacheKey, cacheClass);
	}

	public static void clearAllCacheKeysHavingCacheClass(HttpServletRequest request, Class<?> cacheClass) {
		get(request).clearAllCacheKeysHavingCacheClass(cacheClass);
	}

	public static void clearSessionCache(HttpServletRequest request) {
		get(request).clearSessionScope();
	}

	public static void clearPageCache(HttpServletRequest request) {
		get(request).clearPageScope();
	}

	public static void clearAll(HttpServletRequest request) {
		get(request).clearAll();
	}

	private WebCache() {
	}
}
