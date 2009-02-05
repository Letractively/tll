/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.AccessDecisionManager;
import org.springframework.security.AuthenticationManager;

import com.tll.dao.DaoMode;
import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
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
public final class RequestContext implements IAppContext, ISecurityContext {

	private final IAppContext appContext;
	private final ISecurityContext securityContext;
	private final HttpServletRequest request;

	/**
	 * Constructor
	 * @param appContext The app context.
	 * @param securityContext The security context.
	 * @param request The http servlet request.
	 */
	public RequestContext(IAppContext appContext, ISecurityContext securityContext, HttpServletRequest request) {
		super();
		if(appContext == null) {
			throw new IllegalArgumentException("No app context specified.");
		}
		if(securityContext == null) {
			throw new IllegalArgumentException("No security context specified.");
		}
		if(request == null) {
			throw new IllegalArgumentException("No http servlet request specified.");
		}
		this.request = request;
		this.appContext = appContext;
		this.securityContext = securityContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpSession getSession() {
		return request.getSession(false);
	}

	@Override
	public boolean isDebug() {
		return appContext.isDebug();
	}

	@Override
	public String getEnvironment() {
		return appContext.getEnvironment();
	}

	@Override
	public IEntityFactory getEntityFactory() {
		return appContext.getEntityFactory();
	}

	@Override
	public Marshaler getMarshaler() {
		return appContext.getMarshaler();
	}

	@Override
	public IEntityServiceFactory getEntityServiceFactory() {
		return appContext.getEntityServiceFactory();
	}

	@Override
	public RefData getAppRefData() {
		return appContext.getAppRefData();
	}

	@Override
	public MailManager getMailManager() {
		return appContext.getMailManager();
	}

	@Override
	public DaoMode getDaoMode() {
		return appContext.getDaoMode();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return appContext.getEntityManagerFactory();
	}

	@Override
	public AuthenticationManager getAuthenticationManager() {
		return securityContext.getAuthenticationManager();
	}

	@Override
	public AccessDecisionManager getHttpRequestAccessDecisionManager() {
		return securityContext.getHttpRequestAccessDecisionManager();
	}

	@Override
	public SecurityMode getSecurityMode() {
		return securityContext.getSecurityMode();
	}

}
