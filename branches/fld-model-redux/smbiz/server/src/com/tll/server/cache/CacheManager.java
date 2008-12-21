/*
 * Created on - Dec 7, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 *
 */

package com.tll.server.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.server.ServletUtil;

/**
 * The HTTP Servlet cache manager. A distinct instance is created for each open
 * HTTP request session.
 * <p>
 * Object refrences may be cached under one of two available scopes: session and
 * page.
 * @author jpk
 */
// TODO make thread safe!!! as the app server may wish to serialize http session
// and the like!
public final class CacheManager {

	private static final Log log = LogFactory.getLog(CacheManager.class);

	public static final int SCOPE_SESSION = 0;
	public static final int SCOPE_PAGE = 1;

	/**
	 * key: cache key, val: cached obj
	 */
	private final Map<String, Object> cache = new HashMap<String, Object>();

	/**
	 * key: cache key, val: Integer (scope)
	 */
	private final Map<String, Integer> scopes = new HashMap<String, Integer>();

	/**
	 * path string of the last SYNCHRONOUS request
	 */
	private String lastSynchronousPath;

	/**
	 * The synchronous path code generator
	 */
	private final IPathCodeGenerator synchronousPathCodeGenerator = new SimplePathCodeGenerator();

	/**
	 * The asynchronous path code generator
	 */
	private final IPathCodeGenerator asynchronousPathCodeGenerator = new RpcPathCodeGenerator();

	/**
	 * Responsible for generating path hash codes from a given http request. This
	 * abstraction is necessary to commonize asynchronous request servlet paths
	 * which may be different within the same page. In other words, we are
	 * maintaining the integrity of {@link com.bazaar.web.CacheManager#SCOPE_PAGE}.
	 * @author jpk
	 */
	private static interface IPathCodeGenerator {

		/**
		 * Does this path code generator support the given servlet path?
		 * @param servletPath
		 * @return true if supported
		 */
		boolean supports(String servletPath);

		/**
		 * Generates a coded path string from a given servlet path.
		 * @param servletPath the servlet path.
		 * @return the coded path string.
		 */
		String generatePathCode(String servletPath);
	}

	/**
	 * SimplePathCodeGenerator - Used for synchronous server calls.
	 * @author jpk
	 */
	protected static class SimplePathCodeGenerator implements IPathCodeGenerator {

		public String generatePathCode(String servletPath) {
			return Integer.toString(servletPath.hashCode());
		}

		public boolean supports(String servletPath) {
			return !servletPath.startsWith("/rpc/");
		}

	}

	/**
	 * Generates path code from an asynchronous (rpc) http request.
	 * <p>
	 * GWT based servlet path FORMAT: /rpc/exec/AnRpcService
	 * @author jpk
	 */
	protected static class RpcPathCodeGenerator implements IPathCodeGenerator {

		public boolean supports(String servletPath) {
			return servletPath.startsWith("/rpc/");
		}

		public String generatePathCode(String servletPath) {
			return 'A' + Integer.toString(servletPath.hashCode());
			// NOTE: the A denotes asynchronous
		}
	}

	/**
	 * @param servletPath
	 * @return the applicable {link {@link IPathCodeGenerator} for this servlet
	 *         path.
	 */
	private IPathCodeGenerator getPathCodeGenerator(String servletPath) {
		return asynchronousPathCodeGenerator.supports(servletPath) ? asynchronousPathCodeGenerator
				: synchronousPathCodeGenerator;
	}

	/**
	 * Generate the cache ready path code for this request.
	 * @param request
	 * @return unique path code string.
	 */
	private String getPathCode(HttpServletRequest request) {
		return getPathCode(ServletUtil.getPath(request));
	}

	/**
	 * Generate the cache ready path code for this servlet path.
	 * @param servletPath the servlet path.
	 * @return unique path code string.
	 */
	private String getPathCode(String servletPath) {
		return getPathCodeGenerator(servletPath).generatePathCode(servletPath);
	}

	/**
	 * Generates a string key from the request's path and string key.
	 * <p>
	 * FORMAT { path hash code string }-{ key hash code string }
	 */
	public String getPageCacheKey(HttpServletRequest request, String key) {
		return getPathCode(request) + "-" + Integer.toString((key == null ? "NULL" : key).hashCode());
	}

	/**
	 * Generates a string key from the request's path and class argument.
	 * <p>
	 * FORMAT { path hash code string }-{ cache class hash code string }
	 */
	public String getPageCacheKey(HttpServletRequest request, Class<?> cacheClass) {
		return getPathCode(request) + "-" + getClassHashCodeString(cacheClass);
	}

	private String getClassHashCodeString(Class<?> cacheClass) {
		return Integer.toString(cacheClass.hashCode());
	}

	/**
	 * Generates a string key from the request's path, the string key and class
	 * arguments.
	 * <p>
	 * FORMAT { path hash code string }-{ key hash code string }-{ cache class
	 * hash code string }
	 */
	public String getPageCacheKey(HttpServletRequest request, String key, Class<?> cacheClass) {
		return getPathCode(request) + "-" + getCacheKey(key, cacheClass);
	}

	/**
	 * Generates a string key the string key and class arguments.
	 * <p>
	 * FORMAT { key hash code string }-{ cache class hash code string }
	 */
	public String getCacheKey(String key, Class<?> cacheClass) {
		return Integer.toString((key == null ? "NULL" : key).hashCode()) + "-" + Integer.toString(cacheClass.hashCode());
	}

	/*
	 * retrieve
	 */

	public Object retrieve(HttpServletRequest request, Class<?> cacheClass) {
		return retrieve(getPageCacheKey(request, cacheClass));
	}

	public Object retrieve(HttpServletRequest request, String key) {
		return retrieve(getPageCacheKey(request, key));
	}

	public Object retrieve(HttpServletRequest request, String key, Class<?> cacheClass) {
		return retrieve(getPageCacheKey(request, key, cacheClass));
	}

	public Object retrieveWithCacheKey(HttpServletRequest request, String cacheKey) {
		return retrieve(cacheKey);
	}

	public Object retrieveWithCacheKeys(String key, Class<?> cacheClass) {
		return retrieve(getCacheKey(key, cacheClass));
	}

	/*
	 * store
	 */

	/**
	 * Single method to store object references in the cache manager given a
	 * scope. Returns the given cache key argument under which the object was
	 * stored for convenience.
	 */
	private String storeWithCacheKey(String cacheKey, Object obj, int scope) {
		store(cacheKey, obj, scope);
		return cacheKey;
	}

	/**
	 * Stores the object under a key that is generated from the request and key
	 * arguments.
	 * @param request
	 * @param key
	 * @param obj
	 * @return the generated cache key
	 */
	public String storePageScope(HttpServletRequest request, String key, Object obj) {
		return storeWithCacheKey(getPageCacheKey(request, key), obj, SCOPE_PAGE);
	}

	/**
	 * Stores the object under a key that is generated from the request and cache
	 * class arguments.
	 * @param request
	 * @param cacheClass
	 * @param obj
	 * @return the generated cache key
	 */
	public String storePageScope(HttpServletRequest request, Class<?> cacheClass, Object obj) {
		return storeWithCacheKey(getPageCacheKey(request, cacheClass), obj, SCOPE_PAGE);
	}

	/**
	 * Stores the object under a key that is generated from the request, key and
	 * cache class arguments.
	 * @param request
	 * @param key
	 * @param cacheClass
	 * @param obj
	 * @return the generated cache key
	 */
	public String storePageScope(HttpServletRequest request, String key, Class<?> cacheClass, Object obj) {
		return storeWithCacheKey(getPageCacheKey(request, key, cacheClass), obj, SCOPE_PAGE);
	}

	/**
	 * Stores the given object reference under the given cache key. NOTE this
	 * method differs from the other store methods in that the given key is taken
	 * to be the actual cache key.
	 * @param request
	 * @param cacheKey
	 * @param obj
	 */
	public void storePageScopeWithCacheKey(HttpServletRequest request, String cacheKey, Object obj) {
		storeWithCacheKey(cacheKey, obj, SCOPE_PAGE);
	}

	/**
	 * Stores the given object reference under the given cache key. NOTE this
	 * method differs from the other store methods in that the given key is taken
	 * to be the actual cache key.
	 * @param request
	 * @param cacheKey
	 * @param cacheClass
	 * @param obj
	 * @return the cache key
	 */
	public String storePageScopeWithCacheKeys(HttpServletRequest request, String cacheKey, Class<?> cacheClass, Object obj) {
		return storeWithCacheKey(getCacheKey(cacheKey, cacheClass), obj, SCOPE_PAGE);
	}

	/*
	 * store session scope
	 */

	public String storeSessionScope(HttpServletRequest request, String key, Object obj) {
		return storeWithCacheKey(getPageCacheKey(request, key), obj, SCOPE_SESSION);
	}

	public String storeSessionScope(HttpServletRequest request, Class<?> cacheClass, Object obj) {
		return storeWithCacheKey(getPageCacheKey(request, cacheClass), obj, SCOPE_SESSION);
	}

	public String storeSessionScope(HttpServletRequest request, String key, Class<?> cacheClass, Object obj) {
		return storeWithCacheKey(getPageCacheKey(request, key, cacheClass), obj, SCOPE_SESSION);
	}

	public void storeSessionScopeWithCacheKey(String cacheKey, Object obj) {
		storeWithCacheKey(cacheKey, obj, SCOPE_SESSION);
	}

	public String storeSessionScopeWithCacheKeys(String cacheKey, Class<?> cacheClass, Object obj) {
		return storeWithCacheKey(getCacheKey(cacheKey, cacheClass), obj, SCOPE_SESSION);
	}

	/*
	 * clear
	 */

	public Object clear(HttpServletRequest request, String key) {
		return clear(getPageCacheKey(request, key));
	}

	public Object clear(HttpServletRequest request, Class<?> cacheClass) {
		return clear(getPageCacheKey(request, cacheClass));
	}

	public Object clear(HttpServletRequest request, String key, Class<?> cacheClass) {
		return clear(getPageCacheKey(request, key, cacheClass));
	}

	public Object clearWithCacheKey(String cacheKey) {
		return clear(cacheKey);
	}

	public Object clearWithCacheKeys(String cacheKey, Class<?> cacheClass) {
		return clear(getCacheKey(cacheKey, cacheClass));
	}

	public void clearAllCacheKeysHavingCacheClass(Class<?> cacheClass) {
		List<String> toRmv = new ArrayList<String>();
		for(String cacheKey : cache.keySet()) {
			String substr = "-" + getClassHashCodeString(cacheClass);
			if(cacheKey.indexOf(substr) > 0) {
				toRmv.add(cacheKey);
			}
		}
		remove(toRmv);
	}

	public String getLastSynchronousPath() {
		return lastSynchronousPath;
	}

	private Object retrieve(String key) {
		return cache.get(key);
	}

	private void store(String key, Object obj, int scope) {
		if(key == null || key.length() < 1) {
			throw new IllegalArgumentException("Unable to store object: Empty cache key specified.");
		}
		cache.put(key, obj);
		scopes.put(key, new Integer(scope));
		if(log.isDebugEnabled())
			log.debug("Stored "
					+ (obj == null ? "NULL" : ClassUtils.getShortClassName(obj.getClass()) + " under key: " + key));
	}

	private Object clear(String key) {
		if(key == null || key.length() < 1) return null;
		if(!cache.containsKey(key)) {
			if(log.isDebugEnabled()) log.warn("No cached object found under key: '" + key + "' for clearing.");
			return null;
		}
		Object co = cache.remove(key);
		scopes.remove(key);
		if(log.isDebugEnabled()) log.debug("Cleared cache object under key: " + key);
		return co;
	}

	public void clearAll() {
		cache.clear();
		scopes.clear();
	}

	private void remove(List<String> toRmv) {
		if(toRmv != null && toRmv.size() > 0) {
			scopes.keySet().removeAll(toRmv);
			cache.keySet().removeAll(toRmv);
			if(log.isDebugEnabled()) log.debug("Removed " + toRmv.size() + " cached objects");
		}
	}

	public void clearSessionScope() {
		List<String> toRmv = new ArrayList<String>();
		for(String key : scopes.keySet()) {
			Integer scope = scopes.get(key);
			if(scope.intValue() == SCOPE_SESSION) {
				toRmv.add(key);
			}
		}
		remove(toRmv);
	}

	public void clearPageScope() {
		List<String> toRmv = new ArrayList<String>();
		for(String key : scopes.keySet()) {
			Integer scope = scopes.get(key);
			if(scope.intValue() == SCOPE_PAGE) {
				toRmv.add(key);
			}
		}
		remove(toRmv);
	}

	/**
	 * Updates the cache clearing out page scoped objects given a path code.
	 * @param request
	 */
	public void updateCache(HttpServletRequest request) {
		final String servletPath = ServletUtil.getPath(request);
		final String pathCode = getPathCode(servletPath);

		if(asynchronousPathCodeGenerator.supports(servletPath)) {
			// no-op (we're on the same page - an AJAX call)
			return;
		}

		if(lastSynchronousPath == null) {
			lastSynchronousPath = servletPath;
			return;
		}

		if(!pathCode.equals(getPathCodeGenerator(lastSynchronousPath).generatePathCode(lastSynchronousPath))
				&& !cache.isEmpty()) {
			clearPageScope();
		}

		lastSynchronousPath = servletPath;
	}

}
