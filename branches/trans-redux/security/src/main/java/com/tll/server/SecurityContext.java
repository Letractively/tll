/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;

/**
 * SecurityContext - {@link ISecurityContext} impl.
 * @author jpk
 */
public class SecurityContext implements ISecurityContext {

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

	@Override
	public SecurityMode getSecurityMode() {
		return securityMode;
	}

	@Override
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	@Override
	public AccessDecisionManager getHttpRequestAccessDecisionManager() {
		return httpRequestAccessDecisionManager;
	}
}
