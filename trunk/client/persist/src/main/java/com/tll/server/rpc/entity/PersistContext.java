/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server.rpc.entity;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.tll.mail.MailManager;
import com.tll.model.IEntityAssembler;
import com.tll.model.schema.ISchemaInfo;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.IExceptionHandler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * PersistContext - Servlet context scoped data object holding needed types for
 * processing persist/model data related functionality.
 * @author jpk
 */
public final class PersistContext implements Serializable {

	private static final long serialVersionUID = 7366163949288867262L;

	/**
	 * The key identifying the {@link PersistContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(serialVersionUID);

	private final RefData refData;
	private final MailManager mailManager;
	private final ISchemaInfo schemaInfo;
	private final Marshaler marshaler;
	private final EntityManagerFactory entityManagerFactory;
	private final IEntityTypeResolver entityTypeResolver;
	private final IEntityAssembler entityAssembler;
	private final IEntityServiceFactory entityServiceFactory;
	private final INamedQueryResolver namedQueryResolver;
	private final IExceptionHandler exceptionHandler;


	/**
	 * Constructor
	 * @param refData
	 * @param mailManager
	 * @param schemaInfo
	 * @param marshaler
	 * @param entityManagerFactory
	 * @param entityTypeResolver
	 * @param entityAssembler
	 * @param entityServiceFactory
	 * @param namedQueryResolver
	 * @param exceptionHandler
	 */
	@Inject
	public PersistContext(RefData refData, MailManager mailManager, ISchemaInfo schemaInfo, Marshaler marshaler,
			EntityManagerFactory entityManagerFactory, IEntityTypeResolver entityTypeResolver,
			IEntityAssembler entityAssembler, IEntityServiceFactory entityServiceFactory,
			INamedQueryResolver namedQueryResolver, IExceptionHandler exceptionHandler) {
		super();
		this.refData = refData;
		this.mailManager = mailManager;
		this.schemaInfo = schemaInfo;
		this.marshaler = marshaler;
		this.entityManagerFactory = entityManagerFactory;
		this.entityTypeResolver = entityTypeResolver;
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

	public ISchemaInfo getSchemaInfo() {
		return schemaInfo;
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

	public IEntityTypeResolver getEntityTypeResolver() {
		return entityTypeResolver;
	}

}
