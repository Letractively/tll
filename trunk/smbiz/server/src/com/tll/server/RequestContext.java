/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Injector;
import com.tll.mail.MailManager;
import com.tll.model.EntityAssembler;
import com.tll.refdata.AppRefData;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * RequestContext - Provides servlets w/ the needed constructs to perform the
 * requested job including a ref to the http request.
 * <p>
 * In particular, this construct becomes necessary when the request is delegated
 * to a class that does NOT have access to the http request.
 * @author jpk
 */
public final class RequestContext {

	private final HttpServletRequest request;
	private final ServletContext servletContext;
	private final Injector injector;

	/**
	 * Constructor
	 * @param request Can not be <code>null<code>.
	 * @param servletContext
	 * @param injector
	 */
	public RequestContext(HttpServletRequest request, ServletContext servletContext, Injector injector) {
		super();
		if(request == null) {
			throw new IllegalArgumentException("The http servlet request can not be null");
		}
		if(servletContext == null) {
			throw new IllegalArgumentException("The servlet context can not be null");
		}
		if(injector == null) {
			throw new IllegalArgumentException("The injector can not be null");
		}
		this.request = request;
		this.servletContext = servletContext;
		this.injector = injector;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public HttpSession getSession() {
		return request.getSession(false);
	}

	public boolean isDebug() {
		return ((Boolean) getServletContext().getAttribute(Constants.IS_DEBUG_CONTEXT_ATTRIBUTE)).booleanValue();
	}

	public String getEnvironment() {
		return (String) getServletContext().getAttribute(Constants.ENVIRONMENT_CONTEXT_ATTRIBUTE);
	}

	public EntityAssembler getEntityAssembler() {
		return getInstance(EntityAssembler.class);
	}

	public Marshaler getMarshaler() {
		return getInstance(Marshaler.class);
	}

	public IEntityServiceFactory getEntityServiceFactory() {
		return getInstance(IEntityServiceFactory.class);
	}

	public AppRefData getAppRefData() {
		return getInstance(AppRefData.class);
	}

	public MailManager getMailManager() {
		return getInstance(MailManager.class);
	}

	private <T> T getInstance(Class<T> type) {
		return injector.getInstance(type);
	}
}
