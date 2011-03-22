/**
 * The Logic Lab
 * @author jpk Jan 30, 2009
 */
package com.tll.server;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.tll.mail.MailManager;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.SchemaInfo;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * Servlet context scoped data object holding needed types for processing
 * persist/model data related functionality.
 * @author jpk
 */
public final class PersistContext {

	/**
	 * The key identifying the {@link PersistContext} in the
	 * {@link ServletContext}.
	 */
	public static final String KEY = Long.toString(7366163949288867262L);

	private final MailManager mailManager;
	private final SchemaInfo schemaInfo;
	private final IEntityFactory entityFactory;
	private final IEntityAssembler entityAssembler;
	private final IEntityServiceFactory entityServiceFactory;
	private final IExceptionHandler exceptionHandler;

	/**
	 * Constructor
	 * @param mailManager
	 * @param schemaInfo
	 * @param entityFactory
	 * @param entityAssembler
	 * @param entityServiceFactory
	 * @param exceptionHandler
	 */
	@Inject
	public PersistContext(MailManager mailManager, SchemaInfo schemaInfo, IEntityFactory entityFactory,
			IEntityAssembler entityAssembler, IEntityServiceFactory entityServiceFactory, IExceptionHandler exceptionHandler) {
		super();
		this.mailManager = mailManager;
		this.schemaInfo = schemaInfo;
		this.entityFactory = entityFactory;
		this.entityAssembler = entityAssembler;
		this.entityServiceFactory = entityServiceFactory;
		this.exceptionHandler = exceptionHandler;
	}

	public IEntityServiceFactory getEntityServiceFactory() {
		return entityServiceFactory;
	}

	public IEntityFactory getEntityFactory() {
		return entityFactory;
	}

	public IEntityAssembler getEntityAssembler() {
		return entityAssembler;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public SchemaInfo getSchemaInfo() {
		return schemaInfo;
	}

	public IExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
}
