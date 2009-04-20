/**
 * The Logic Lab
 */
package com.tll.server.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.context.SecurityContextHolder;

import com.tll.model.User;
import com.tll.server.AdminContext;

/**
 * SmbizAuthenticationProcessingFilter
 * @author jpk
 */
public final class SmbizAuthenticationProcessingFilter extends AuthenticationProcessingFilter {
	
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) /*throws IOException*/{

		// create an AdminContext for this servlet session
		log.debug("Creating admin context from acegi security context..");
		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		assert user != null;
		final AdminContext ac = new AdminContext();
		ac.setUser(user);
		ac.setAccount(user.getAccount()); // default set the current account to the
		// user's owning account

		request.getSession(false).setAttribute(AdminContext.KEY, ac);
	}

	@Override
	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) /*throws IOException*/{
		log.debug("User authentication failed");
		// no-op
	}
}
