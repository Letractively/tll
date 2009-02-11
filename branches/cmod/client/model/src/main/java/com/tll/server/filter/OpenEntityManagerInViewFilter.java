/**
 * The Logic Lab
 * @author jpk Aug 26, 2007
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tll.dao.DaoMode;
import com.tll.server.rpc.entity.IMEntityServiceContext;

/**
 * OpenEntityManagerInViewFilter
 * @author jpk
 */
public class OpenEntityManagerInViewFilter extends org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter {

	EntityManagerFactory emf;

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();

		FilterConfig config = this.getFilterConfig();
		IMEntityServiceContext context =
				(IMEntityServiceContext) config.getServletContext().getAttribute(IMEntityServiceContext.SERVLET_CONTEXT_KEY);
		if(context.getDaoMode() == DaoMode.ORM) {
			if((emf = context.getEntityManagerFactory()) == null) {
				throw new Error("Can't obtain entity manager factory reference.");
			}
		}
	}

	@Override
	protected EntityManagerFactory lookupEntityManagerFactory(HttpServletRequest request) {
		return emf;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// only do open session in view filter when the entity manager factory
		// instance is non-null (we may be in mock dao mode)
		if(this.emf != null) {
			super.doFilterInternal(request, response, filterChain);
		}
		else {
			filterChain.doFilter(request, response);
		}
	}

}
