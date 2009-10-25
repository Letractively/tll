/**
 * The Logic Lab
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationProvider;
import org.springframework.security.userdetails.memory.UserAttribute;
import org.springframework.security.userdetails.memory.UserAttributeEditor;

/**
 * AnonymousProcessingFilter
 * @author jpk
 */
public class AnonymousProcessingFilter extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(AnonymousProcessingFilter.class);

	/**
	 * The wrapped {@link AnonymousAuthenticationProvider}.
	 */
	private final org.springframework.security.providers.anonymous.AnonymousProcessingFilter wrapped =
			new org.springframework.security.providers.anonymous.AnonymousProcessingFilter();

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Initializing AnonymousProcessingFilter..");
		final String k = config.getInitParameter("key");
		if(k == null) {
			throw new ServletException("The init parameter 'key' must be declared");
		}
		wrapped.setKey(k);

		final String ua = config.getInitParameter("userAttribute");
		if(ua == null) {
			throw new Error("The init parameter 'userAttribute' must be declared");
		}
		final UserAttributeEditor uae = new UserAttributeEditor();
		uae.setAsText(ua);
		final UserAttribute userAttribute = (UserAttribute) uae.getValue();
		wrapped.setUserAttribute(userAttribute);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	ServletException {
		log.debug("AnonymousProcessingFilter filtering..");
		wrapped.doFilter(request, response, chain);
	}

}
