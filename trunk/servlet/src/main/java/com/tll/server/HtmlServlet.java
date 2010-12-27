/**
 * The Logic Lab
 */
package com.tll.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HtmlServlet - Handles *.html submissions
 * @author jpk
 */
public class HtmlServlet extends HttpServlet {
	private static final long serialVersionUID = 5991556813541604367L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
		String path = req.getRequestURI();
		
		// get the static file
    /*
    path = StringUtils.replace(path, "/amc/test", "/htmltest");
    if( path.endsWith("/")) {
        path += "index.html";
    }
    */
    req.getRequestDispatcher(path).forward(req, resp);
    
	}

}
