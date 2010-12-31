/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.common.model.IEntityTypeResolver;
import com.tll.mail.MailManager;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.SchemaInfo;
import com.tll.server.IExceptionHandler;
import com.tll.server.marshal.IMarshalOptionsResolver;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.PersistCache;
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
	 * Responsible for binding the needed {@link IPersistServiceImplResolver}
	 * implementation.
	 */
	protected abstract Class<? extends IPersistServiceImplResolver> getPersistServiceImplResolverType();

	@Override
	protected void configure() {
		log.info("Employing Client Persistence module");

		// IPersistServiceImplResolver
		bind(IPersistServiceImplResolver.class).to(getPersistServiceImplResolverType()).in(Scopes.SINGLETON);

		// manually construct the persist context instance since we have optional
		// constructor params

		// PersistContext
		bind(PersistContext.class).toProvider(new Provider<PersistContext>() {

			@Inject(optional = true)
			MailManager mailManager;
			@Inject
			SchemaInfo schemaInfo;
			@Inject
			IEntityTypeResolver entityTypeResolver;
			@Inject
			IEntityFactory entityFactory;
			@Inject
			IEntityAssembler entityAssembler;
			@Inject
			IEntityServiceFactory entityServiceFactory;
			@Inject
			IExceptionHandler exceptionHandler;
			@Inject
			PersistCache persistCache;
			@Inject
			Marshaler marshaler;
			@Inject
			IMarshalOptionsResolver marshalOptionsResolver;
			

			@Override
			public PersistContext get() {
				return new PersistContext(mailManager, schemaInfo, marshaler, marshalOptionsResolver, entityTypeResolver, entityFactory, entityAssembler,
						entityServiceFactory, exceptionHandler, persistCache);
			}
		}).in(Scopes.SINGLETON);

		// PersistServiceDelegate
		bind(PersistServiceDelegate.class).in(Scopes.SINGLETON);
	}
}
