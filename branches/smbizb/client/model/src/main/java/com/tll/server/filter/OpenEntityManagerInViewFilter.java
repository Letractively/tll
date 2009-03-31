/**
 * The Logic Lab
 * @author jpk Aug 26, 2007
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.tll.server.rpc.entity.MEntityContext;

/**
 * OpenEntityManagerInViewFilter - Re-dux of Spring's version but w/o its
 * application context stuff.
 * <p>
 * NOTE: The impl supports the case where there is no specified
 * {@link EntityManagerFactory} which, presumably, implies we are operating in
 * "mock" mode. In this case, this filter is a pass through doing nothing.
 * @author jpk
 */
public class OpenEntityManagerInViewFilter implements Filter {

	private static final Log logger = LogFactory.getLog(OpenEntityManagerInViewFilter.class);

	EntityManagerFactory emf;

	@Override
	public void init(FilterConfig config) /*throws ServletException*/{
		final MEntityContext context =
				(MEntityContext) config.getServletContext().getAttribute(MEntityContext.KEY);
			emf = context.getEntityManagerFactory();
	}

	@Override
	public void destroy() {
		if(emf != null) {
			emf.close();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		// only do open session in view filter when the entity manager factory
		// instance is non-null (we may be in mock dao mode)
		if(this.emf != null) {
			boolean participate = false;

			if(TransactionSynchronizationManager.hasResource(emf)) {
				// Do not modify the EntityManager: just set the participate flag.
				participate = true;
			}
			else {
				logger.debug("Opening JPA EntityManager in OpenEntityManagerInViewFilter");
				try {
					final EntityManager em = emf.createEntityManager();
					TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));
				}
				catch(final PersistenceException ex) {
					throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
				}
			}

			try {
				chain.doFilter(request, response);
			}

			finally {
				if(!participate) {
					final EntityManagerHolder emHolder =
							(EntityManagerHolder) TransactionSynchronizationManager.unbindResource(emf);
					logger.debug("Closing JPA EntityManager in OpenEntityManagerInViewFilter");
					EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
				}
			}
		}
		else {
			// pass through (no-op)
			chain.doFilter(request, response);
		}
	}
}
