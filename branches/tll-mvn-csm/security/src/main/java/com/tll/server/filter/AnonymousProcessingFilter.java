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

import org.springframework.security.providers.anonymous.AnonymousAuthenticationProvider;
import org.springframework.security.userdetails.memory.UserAttribute;
import org.springframework.security.userdetails.memory.UserAttributeEditor;

import com.tll.server.ISecurityContext;

/**
 * AnonymousProcessingFilter
 * @author jpk
 */
public class AnonymousProcessingFilter extends AbstractSecurityFilter {

	/**
	 * The wrapped {@link AnonymousAuthenticationProvider}.
	 */
	private org.springframework.security.providers.anonymous.AnonymousProcessingFilter wrapped;

	@Override
	public void doInitAcegi(FilterConfig config, ISecurityContext securityContext) /*throws ServletException*/{
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

	@Override
	public void doFilterAcegi(ServletRequest request, ServletResponse response, FilterChain chain,
			ISecurityContext securityContext) throws IOException,
			ServletException {
		wrapped.doFilter(request, response, chain);
	}

}
