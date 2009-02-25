/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;

/**
 * SecurityContext - {@link SecurityContext} impl.
 * @author jpk
 */
public class SecurityContext {

	/**
	 * The key identifying the sole {@link SecurityContext} in the
	 * {@link ServletContext}.
	 */
	public static final String SERVLET_CONTEXT_KEY = SecurityContext.class.getName();
	
	private final SecurityMode securityMode;
	private final AuthenticationManager authenticationManager;
	private final AccessDecisionManager httpRequestAccessDecisionManager;

	/**
	 * Constructor
	 * @param securityMode
	 * @param authenticationManager
	 * @param httpRequestAccessDecisionManager
	 */
	public SecurityContext(SecurityMode securityMode, AuthenticationManager authenticationManager,
			AccessDecisionManager httpRequestAccessDecisionManager) {
		super();
		this.securityMode = securityMode;
		this.authenticationManager = authenticationManager;
		this.httpRequestAccessDecisionManager = httpRequestAccessDecisionManager;
	}

	public SecurityMode getSecurityMode() {
		return securityMode;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public AccessDecisionManager getHttpRequestAccessDecisionManager() {
		return httpRequestAccessDecisionManager;
	}
}
