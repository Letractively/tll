/**
 * The Logic Lab
 * @author jpk Dec 24, 2007
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AuthenticationManager;

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
public final class RequestContext implements IAppContext {

	private final IAppContext appContext;
	private final HttpServletRequest request;

	/**
	 * Constructor
	 * @param appContext The app context.
	 * @param request The http servlet request.
	 */
	public RequestContext(IAppContext appContext, HttpServletRequest request) {
		super();
		if(appContext == null) {
			throw new IllegalArgumentException("No app context specified.");
		}
		if(request == null) {
			throw new IllegalArgumentException("No http servlet request specified.");
		}
		this.request = request;
		this.appContext = appContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpSession getSession() {
		return request.getSession(false);
	}

	public boolean isDebug() {
		return appContext.isDebug();
	}

	public String getEnvironment() {
		return appContext.getEnvironment();
	}

	public IEntityFactory getEntityFactory() {
		return appContext.getEntityFactory();
	}

	public Marshaler getMarshaler() {
		return appContext.getMarshaler();
	}

	public IEntityServiceFactory getEntityServiceFactory() {
		return appContext.getEntityServiceFactory();
	}

	public RefData getAppRefData() {
		return appContext.getAppRefData();
	}

	public MailManager getMailManager() {
		return appContext.getMailManager();
	}

	@Override
	public SecurityMode getSecurityMode() {
		return appContext.getSecurityMode();
	}

	@Override
	public AuthenticationManager getAuthenticationManager() {
		return appContext.getAuthenticationManager();
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
	public AccessDecisionManager getHttpRequestAccessDecisionManager() {
		return appContext.getHttpRequestAccessDecisionManager();
	}
}
