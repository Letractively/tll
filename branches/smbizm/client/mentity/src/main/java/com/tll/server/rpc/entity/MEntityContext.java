/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server.rpc.entity;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import com.tll.mail.MailManager;
import com.tll.model.IEntityAssembler;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.IExceptionHandler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * MEntityContext - Servlet context scoped data object holding needed types for
 * processing mentity related server side routines.
 * @author jpk
 */
public final class MEntityContext implements Serializable {

	private static final long serialVersionUID = 7366163949288867262L;

	/**
	 * The key identifying the {@link MEntityContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(serialVersionUID);

	private final RefData refData;
	private final MailManager mailManager;
	private final Marshaler marshaler;
	private final EntityManagerFactory entityManagerFactory;
	private final IEntityAssembler entityAssembler;
	private final IEntityServiceFactory entityServiceFactory;
	private final INamedQueryResolver namedQueryResolver;
	private final IExceptionHandler exceptionHandler;

	/**
	 * Constructor
	 * @param refData
	 * @param mailManager
	 * @param marshaler
	 * @param entityManagerFactory
	 * @param entityAssembler
	 * @param entityServiceFactory
	 * @param namedQueryResolver
	 * @param exceptionHandler
	 */
	public MEntityContext(RefData refData, MailManager mailManager, Marshaler marshaler,
			EntityManagerFactory entityManagerFactory, IEntityAssembler entityAssembler,
			IEntityServiceFactory entityServiceFactory, INamedQueryResolver namedQueryResolver, IExceptionHandler exceptionHandler) {
		super();
		this.refData = refData;
		this.mailManager = mailManager;
		this.marshaler = marshaler;
		this.entityManagerFactory = entityManagerFactory;
		this.entityAssembler = entityAssembler;
		this.entityServiceFactory = entityServiceFactory;
		this.namedQueryResolver = namedQueryResolver;
		this.exceptionHandler = exceptionHandler;
	}

	public IEntityServiceFactory getEntityServiceFactory() {
		return entityServiceFactory;
	}

	public RefData getRefData() {
		return refData;
	}

	public IEntityAssembler getEntityAssembler() {
		return entityAssembler;
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

	public INamedQueryResolver getQueryResolver() {
		return namedQueryResolver;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
}
