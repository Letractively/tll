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
 * ISecurityContext - The security context definition.
 * @author jpk
 */
public interface ISecurityContext {

	/**
	 * The key identifying the sole {@link ISecurityContext} in the
	 * {@link ServletContext}.
	 */
	static final String SERVLET_CONTEXT_KEY = ISecurityContext.class.getName();

	/**
	 * @return the security mode of the app.
	 */
	SecurityMode getSecurityMode();

	/**
	 * @return The security related authentication manager.
	 */
	AuthenticationManager getAuthenticationManager();

	/**
	 * @return The http request access decision manager.
	 */
	AccessDecisionManager getHttpRequestAccessDecisionManager();
}
