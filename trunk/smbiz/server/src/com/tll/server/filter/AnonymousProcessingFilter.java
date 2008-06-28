/**
 * The Logic Lab
 */
package com.tll.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.acegisecurity.userdetails.memory.UserAttribute;
import org.acegisecurity.userdetails.memory.UserAttributeEditor;

import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.server.SecurityMode;

/**
 * AnonymousProcessingFilter
 * @author jpk
 */
public final class AnonymousProcessingFilter extends org.acegisecurity.providers.anonymous.AnonymousProcessingFilter {

	private boolean isAcegi;

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		isAcegi = SecurityMode.ACEGI.name().equals(Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));

		if(isAcegi) {
			String k = config.getInitParameter("key");
			if(k == null) {
				throw new Error("The init parameter 'key' must be declared");
			}
			setKey(k);

			String ua = config.getInitParameter("userAttribute");
			if(ua == null) {
				throw new Error("The init parameter 'userAttribute' must be declared");
			}
			UserAttributeEditor uae = new UserAttributeEditor();
			uae.setAsText(ua);
			UserAttribute userAttribute = (UserAttribute) uae.getValue();
			setUserAttribute(userAttribute);
		}

	}

	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if(isAcegi) {
			super.doFilter(request, response, chain);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
