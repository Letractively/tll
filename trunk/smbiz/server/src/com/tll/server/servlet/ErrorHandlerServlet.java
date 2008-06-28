/**
 * The Logic Lab
 */
package com.tll.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.ui.AccessDeniedHandlerImpl;

/**
 * LoginServlet - Handles login submissions.
 * @author jpk
 */
public class ErrorHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 4786061277023363507L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws /* ServletException, */IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws /* ServletException, */IOException {
		AccessDeniedException ade = (AccessDeniedException) req.getAttribute(AccessDeniedHandlerImpl.ACEGI_SECURITY_ACCESS_DENIED_EXCEPTION_KEY);
		assert ade != null;
		resp.setContentType("text/html");
		resp.getWriter().write(ade.getMessage());
	}

}
