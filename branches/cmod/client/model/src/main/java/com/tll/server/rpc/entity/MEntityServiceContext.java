/**
 * The Logic Lab
 * @author jpk
 * Jan 30, 2009
 */
package com.tll.server.rpc.entity;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.ExceptionHandler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * AppContext - An instance of this type is stored in the {@link ServletContext}
 * providing references to app scoped constructs for use by servlets to fulfill
 * requests.
 * @author jpk
 */
public class MEntityServiceContext implements IMEntityServiceContext {
	
	private final RefData refData;
	private final MailManager mailManager;
	private final Marshaler marshaler;
	private final EntityManagerFactory entityManagerFactory;
	private final IEntityFactory entityFactory;
	private final IEntityServiceFactory entityServiceFactory;
	private final IMEntityServiceImplResolver mEntityServiceImplResolver;
	private final INamedQueryResolver namedQueryResolver;
	private final ExceptionHandler exceptionHandler;

	/**
	 * Constructor
	 * @param refData
	 * @param mailManager
	 * @param marshaler
	 * @param entityManagerFactory
	 * @param entityFactory
	 * @param entityServiceFactory
	 * @param mEntityServiceImplResolver
	 * @param namedQueryResolver
	 * @param exceptionHandler
	 */
	public MEntityServiceContext(RefData refData, MailManager mailManager, Marshaler marshaler,
			EntityManagerFactory entityManagerFactory, IEntityFactory entityFactory,
			IEntityServiceFactory entityServiceFactory, IMEntityServiceImplResolver mEntityServiceImplResolver,
			INamedQueryResolver namedQueryResolver, ExceptionHandler exceptionHandler) {
		super();
		this.refData = refData;
		this.mailManager = mailManager;
		this.marshaler = marshaler;
		this.entityManagerFactory = entityManagerFactory;
		this.entityFactory = entityFactory;
		this.entityServiceFactory = entityServiceFactory;
		this.mEntityServiceImplResolver = mEntityServiceImplResolver;
		this.namedQueryResolver = namedQueryResolver;
		this.exceptionHandler = exceptionHandler;
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
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
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
	public IMEntityServiceImplResolver getServiceResolver() {
		return mEntityServiceImplResolver;
	}

	@Override
	public INamedQueryResolver getQueryResolver() {
		return namedQueryResolver;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
}
