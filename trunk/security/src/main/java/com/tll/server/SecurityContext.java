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
	public static final String KEY = SecurityContext.class.getName();
	
	private final AuthenticationManager authenticationManager;
	private final AccessDecisionManager httpRequestAccessDecisionManager;

	/**
	 * Constructor
	 * @param authenticationManager
	 * @param httpRequestAccessDecisionManager
	 */
	public SecurityContext(AuthenticationManager authenticationManager,
			AccessDecisionManager httpRequestAccessDecisionManager) {
		this.authenticationManager = authenticationManager;
		this.httpRequestAccessDecisionManager = httpRequestAccessDecisionManager;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public AccessDecisionManager getHttpRequestAccessDecisionManager() {
		return httpRequestAccessDecisionManager;
	}
}
