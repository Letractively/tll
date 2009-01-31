/**
 * The Logic Lab
 * @author jpk
 * Jan 30, 2009
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AuthenticationManager;

import com.tll.dao.DaoMode;
import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * AppContext - An instance of this type is stored in the {@link ServletContext}
 * providing references to app scoped constructs for use by servlets to fulfill
 * requests.
 * @author jpk
 */
public class AppContext implements IAppContext {
	
	private final boolean debug;
	private final String environment;
	private final SecurityMode securityMode;
	private final AuthenticationManager authenticationManager;
	private final AccessDecisionManager httpRequesetAccessDecisionManager;
	private final RefData refData;
	private final MailManager mailManager;
	private final Marshaler marshaler;
	private final DaoMode daoMode;
	private final EntityManagerFactory entityManagerFactory;
	private final IEntityFactory entityFactory;
	private final IEntityServiceFactory entityServiceFactory;

	/**
	 * Constructor
	 * @param debug
	 * @param environment
	 * @param securityMode
	 * @param authenticationManager
	 * @param httpRequesetAccessDecisionManager
	 * @param refData
	 * @param mailManager
	 * @param marshaler
	 * @param daoMode
	 * @param entityManagerFactory
	 * @param entityFactory
	 * @param entityServiceFactory
	 */
	public AppContext(boolean debug, String environment, SecurityMode securityMode,
			AuthenticationManager authenticationManager, AccessDecisionManager httpRequesetAccessDecisionManager,
			RefData refData, MailManager mailManager, Marshaler marshaler, DaoMode daoMode,
			EntityManagerFactory entityManagerFactory, IEntityFactory entityFactory,
			IEntityServiceFactory entityServiceFactory) {
		super();
		this.debug = debug;
		this.environment = environment;
		this.securityMode = securityMode;
		this.authenticationManager = authenticationManager;
		this.httpRequesetAccessDecisionManager = httpRequesetAccessDecisionManager;
		this.refData = refData;
		this.mailManager = mailManager;
		this.marshaler = marshaler;
		this.daoMode = daoMode;
		this.entityManagerFactory = entityManagerFactory;
		this.entityFactory = entityFactory;
		this.entityServiceFactory = entityServiceFactory;
	}

	@Override
	public IEntityServiceFactory getEntityServiceFactory() {
		return entityServiceFactory;
	}

	@Override
	public SecurityMode getSecurityMode() {
		return securityMode;
	}

	@Override
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	@Override
	public RefData getAppRefData() {
		return refData;
	}

	@Override
	public IEntityFactory getEntityFactory() {
		return entityFactory;
	}

	@Override
	public String getEnvironment() {
		return environment;
	}

	@Override
	public MailManager getMailManager() {
		return mailManager;
	}

	@Override
	public Marshaler getMarshaler() {
		return marshaler;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public AccessDecisionManager getHttpRequestAccessDecisionManager() {
		return httpRequesetAccessDecisionManager;
	}

	@Override
	public DaoMode getDaoMode() {
		return daoMode;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
}
