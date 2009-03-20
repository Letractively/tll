/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
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
 * MEntityServiceContext
 * @author jpk
 */
public class MEntityContext {

	/**
	 * The key identifying the {@link MEntityContext} in the
	 * {@link ServletContext}.
	 */
	public static final String SERVLET_CONTEXT_KEY = MEntityContext.class.getName();

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
	public MEntityContext(RefData refData, MailManager mailManager, Marshaler marshaler,
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

	public IEntityServiceFactory getEntityServiceFactory() {
		return entityServiceFactory;
	}

	public RefData getRefData() {
		return refData;
	}

	public IEntityFactory getEntityFactory() {
		return entityFactory;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public Marshaler getMarshaler() {
		return marshaler;
	}

	public IMEntityServiceImplResolver getServiceResolver() {
		return mEntityServiceImplResolver;
	}

	public INamedQueryResolver getQueryResolver() {
		return namedQueryResolver;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
}
