/*
 * Created on - Dec 7, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 *
 */

package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.server.ServletUtil;
import com.tll.server.cache.CacheManager;

/**
 * Filter required for managing the {@link CacheManager}.
 * NOTE: Although this is a {@link Filter}, it is located in the cache package not the filter package
 * so that {@link CacheManager} is accessible
 * @author jpk
 */
public class CacheManagerFilter implements Filter {
	
	/**
	 * The key token identifying the {@link CacheManager} in the
	 * {@link HttpSession}.
	 */
	public static final String SA_CACHE_MANAGER = "cm";
	
	private static final Log LOG = LogFactory.getLog(CacheManagerFilter.class);

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		CacheManager cm = getCacheManager(request);
		if(cm != null) {
			cm.updateCache(request);
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig config) /*throws ServletException*/ {
	}

	public void destroy() {
	}

	private CacheManager getCacheManager(HttpServletRequest request) {
		CacheManager cm = null;
		if(request.getSession(false) != null) {
			cm = (CacheManager) ServletUtil.getSessionAttribute(request, SA_CACHE_MANAGER);
			if(cm == null) {
				cm = new CacheManager();
				ServletUtil.setSessionAttribute(request, SA_CACHE_MANAGER, cm);
				LOG.info("Http request CacheManager created.");
			}
		}
		return cm;
	}
}
