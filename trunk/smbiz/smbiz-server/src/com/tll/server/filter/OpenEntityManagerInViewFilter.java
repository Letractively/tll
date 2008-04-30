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

import com.google.inject.Injector;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.dao.DaoMode;
import com.tll.server.Constants;

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
		String daoMode = Config.instance().getString(ConfigKeys.DAO_MODE_PARAM);
		if(!DaoMode.MOCK.name().equals(daoMode)) {
			Injector injector =
					(Injector) config.getServletContext().getAttribute(Constants.GUICE_INJECTOR_CONTEXT_ATTRIBUTE);
			emf = injector.getInstance(EntityManagerFactory.class);
			assert emf != null;
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
