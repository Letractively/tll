/**
 * The Logic Lab
 * @author jkirton
 * @since Mar 21, 2011
 */
package com.tll.server;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.mail.MailManager;
import com.tll.mail.MailModule;
import com.tll.mail.NameEmail;

/**
 * Dependency wiring for the smbiz web app excluding web.xml specific directives
 * which are taken care of elsewhere.
 * @author jpk
 */
public class SmbizWebModule extends AbstractModule {
	
	private final Config config;

	/**
	 * Constructor
	 * @param config
	 */
	public SmbizWebModule(Config config) {
		super();
		if(config == null) throw new IllegalArgumentException("No config specified.");
		this.config = config;
	}

	@Override
	protected void configure() {
		install(new VelocityModule());

		install(new MailModule(config));
		
		bind(IExceptionHandler.class).toProvider(new Provider<IExceptionHandler>() {
			
			@Inject
			MailManager mm;
			
			@Override
			public IExceptionHandler get() {
				String onErrorName = config.getString("server.onerror.ToName");
				String onErrorEmail = config.getString("server.onerror.ToAddress");
				NameEmail onErrorNameEmail = new NameEmail(onErrorName, onErrorEmail);
				return new CompositeExceptionHandler(new LogExceptionHandler(), new EmailExceptionHandler(mm, onErrorNameEmail));
			}
			
		}).in(Scopes.SINGLETON);

		install(new AppModule(config));
		
		bind(PersistContext.class).in(Scopes.SINGLETON);		
	}

}
