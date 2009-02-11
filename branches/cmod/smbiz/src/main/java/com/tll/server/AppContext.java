/**
 * The Logic Lab
 * @author jpk
 * Jan 30, 2009
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

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
	 * @param refData
	 * @param mailManager
	 * @param marshaler
	 * @param daoMode
	 * @param entityManagerFactory
	 * @param entityFactory
	 * @param entityServiceFactory
	 */
	public AppContext(boolean debug, String environment, RefData refData, MailManager mailManager, Marshaler marshaler,
			DaoMode daoMode,
			EntityManagerFactory entityManagerFactory, IEntityFactory entityFactory,
			IEntityServiceFactory entityServiceFactory) {
		super();
		this.debug = debug;
		this.environment = environment;
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
	public RefData getRefData() {
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
	public DaoMode getDaoMode() {
		return daoMode;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
}
