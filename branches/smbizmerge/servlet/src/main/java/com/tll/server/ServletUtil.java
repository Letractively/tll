/*
 * Created on - Nov 15, 2005
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2005 - All rights reserved.
 *
 */

package com.tll.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet utility methods.
 * @author jpk
 */
public abstract class ServletUtil {

	private static final Logger log = LoggerFactory.getLogger(ServletUtil.class);

	/**
	 * @param request
	 * @return the full path: /{servlet name}[/{servlet path}]
	 */
	public static String getPath(final HttpServletRequest request) {

		String s = request.getServletPath();

		if(request.getPathInfo() != null) s += request.getPathInfo();

		if(s.endsWith("/")) {
			s = s.substring(0, s.length() - 1);
		}

		if(log.isDebugEnabled()) log.debug(">>servlet path: " + s);

		return s;
	}

	/**
	 * Sets an HTTP request session attribute NOT creating a session if one
	 * doesn't already exist.
	 * @param request
	 * @param attribName
	 * @param attrib
	 */
	public static void setSessionAttribute(final HttpServletRequest request, final String attribName, final Object attrib) {
		final HttpSession s = request.getSession(false);
		if(s != null) s.setAttribute(attribName, attrib);
	}

	/**
	 * Retrieves an HTTP request session attribute returning <code>null</code> if
	 * either the http request session is null or there is no attribut
	 * @param request
	 * @param attribName
	 * @return the session attribute
	 */
	public static Object getSessionAttribute(final HttpServletRequest request, final String attribName) {
		final HttpSession s = request.getSession(false);
		return s == null ? null : s.getAttribute(attribName);
	}
}
