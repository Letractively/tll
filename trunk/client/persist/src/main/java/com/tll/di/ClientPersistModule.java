/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.di;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.mail.MailManager;
import com.tll.model.IEntityAssembler;
import com.tll.model.schema.ISchemaInfo;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.IExceptionHandler;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.PersistContext;
import com.tll.server.rpc.entity.PersistServiceDelegate;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * ClientPersistModule
 * @author jpk
 */
public abstract class ClientPersistModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(ClientPersistModule.class);

	/**
	 * Constructor
	 */
	public ClientPersistModule() {
		super();
	}

	/**
	 * Responsible for binding the needed {@link IEntityTypeResolver}
	 * implementation.
	 */
	protected abstract void bindEntityTypeResolver();

	/**
	 * Responsible for binding the needed {@link IPersistServiceImplResolver}
	 * implementation.
	 */
	protected abstract void bindPersistServiceImplResolver();

	@Override
	protected final void configure() {
		log.info("Employing Client Persistence module");

		// IEntityTypeResolver
		bindEntityTypeResolver();

		// IPersistServiceImplResolver
		bindPersistServiceImplResolver();

		// manually construct the persist context instance since we have optional
		// constructor params

		// PersistContext
		bind(PersistContext.class).toProvider(new Provider<PersistContext>() {

			@Inject
			RefData refData;
			@Inject(optional = true)
			MailManager mailManager;
			@Inject
			ISchemaInfo schemaInfo;
			@Inject
			Marshaler marshaler;
			@Inject
			IMarshalOptionsResolver marshalOptionsResolver;
			@Inject(optional = true)
			EntityManagerFactory entityManagerFactory;
			@Inject
			IEntityTypeResolver entityTypeResolver;
			@Inject
			IEntityAssembler entityAssembler;
			@Inject
			IEntityServiceFactory entityServiceFactory;
			@Inject
			IExceptionHandler exceptionHandler;

			@Override
			public PersistContext get() {
				return new PersistContext(refData, mailManager, schemaInfo, marshaler, marshalOptionsResolver,
						entityManagerFactory,
						entityTypeResolver, entityAssembler, entityServiceFactory, exceptionHandler);
			}
		}).in(Scopes.SINGLETON);

		// PersistServiceDelegate
		bind(PersistServiceDelegate.class).in(Scopes.SINGLETON);
	}
}
