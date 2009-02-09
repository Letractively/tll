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

import com.tll.server.ISecurityContext;
import com.tll.server.SecurityMode;

/**
 * AnonymousProcessingFilter
 * @author jpk
 */
public class AnonymousProcessingFilter extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(AnonymousProcessingFilter.class);
	
	/**
	 * The wrapped {@link AnonymousAuthenticationProvider}.
	 */
	private org.springframework.security.providers.anonymous.AnonymousProcessingFilter wrapped;

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.debug("Initializing AnonymousProcessingFilter..");
		final ISecurityContext sc = getSecurityContext(config);

		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
			String k = config.getInitParameter("key");
			if(k == null) {
				throw new Error("The init parameter 'key' must be declared");
			}
			wrapped.setKey(k);

			String ua = config.getInitParameter("userAttribute");
			if(ua == null) {
				throw new Error("The init parameter 'userAttribute' must be declared");
			}
			UserAttributeEditor uae = new UserAttributeEditor();
			uae.setAsText(ua);
			UserAttribute userAttribute = (UserAttribute) uae.getValue();
			wrapped.setUserAttribute(userAttribute);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		log.debug("AnonymousProcessingFilter filtering..");
		ISecurityContext sc = getSecurityContext(request);
		if(sc.getSecurityMode() == SecurityMode.ACEGI) {
			wrapped.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
