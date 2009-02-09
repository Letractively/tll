/**
 * The Logic Lab
 * @author jpk Nov 20, 2007
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.config.Config;
import com.tll.di.SecurityModule.ConfigKeys;
import com.tll.server.SecurityMode;
import com.tll.util.EnumUtil;

/**
 * HttpSessionContextIntegrationFilter
 * @author jpk
 */
public class HttpSessionContextIntegrationFilter extends AbstractSecurityFilter {

	protected static final Log log = LogFactory.getLog(HttpSessionContextIntegrationFilter.class);
	
	/**
	 * The wrapped
	 * {@link org.springframework.security.context.HttpSessionContextIntegrationFilter}
	 * .
	 */
	private org.springframework.security.context.HttpSessionContextIntegrationFilter wrapped;

	@Override
	public void init(FilterConfig config) /*throws ServletException*/{
		// no-op
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		final SecurityMode securityMode =
				EnumUtil.fromString(SecurityMode.class, Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));
		log.debug("HttpSessionContextIntegrationFilter (SecurityMode: " + securityMode + ") filtering..");
		if(securityMode == SecurityMode.ACEGI) {
			wrapped.doFilter(request, response, chain);
		}
		else {
			// force session creation
			((HttpServletRequest) request).getSession(true);
			chain.doFilter(request, response);
		}
	}
}
