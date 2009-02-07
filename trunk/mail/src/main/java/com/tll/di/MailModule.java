/*
 * The Logic Lab 
 */
package com.tll.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.tll.config.Config;
import com.tll.mail.IComposer;
import com.tll.mail.IMailContext;
import com.tll.mail.IMailSender;
import com.tll.mail.MailRouting;
import com.tll.mail.MailSender;
import com.tll.mail.NameEmail;
import com.tll.mail.SimpleComposer;
import com.tll.mail.TemplateComposer;

/**
 * MailModule
 * @author jpk
 */
public final class MailModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(MailModule.class);

	/**
	 * Constructor
	 */
	public MailModule() {
		super();
		log.info("Employing mail module");
	}

	/**
	 * DefaultMailRouting annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface DefaultMailRouting {
	}

	/**
	 * PrimaryMailSender annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface PrimaryMailSender {
	}

	/**
	 * SecondaryMailSender annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface SecondaryMailSender {
	}

	@Override
	protected void configure() {
		Names.bindProperties(this.binder(), Config.instance().asMap("mail", "mail."));

		// default mail routing object
		bind(Key.get(MailRouting.class, DefaultMailRouting.class)).toProvider(new Provider<MailRouting>() {

			public MailRouting get() {
				final NameEmail dfltSender = new NameEmail(dfltFromName, dfltFromAddress);
				final NameEmail dfltRecipient = new NameEmail(dfltToName, dfltToAddress);
				final MailRouting mr = new MailRouting();
				mr.setSender(dfltSender);
				mr.addRecipient(dfltRecipient);
				return mr;
			}

			@Inject
			@Named("mail.default.FromName")
			String dfltFromName;
			@Inject
			@Named("mail.default.FromAddress")
			String dfltFromAddress;
			@Inject
			@Named("mail.default.ToName")
			String dfltToName;
			@Inject
			@Named("mail.default.ToAddress")
			String dfltToAddress;

		}).in(Scopes.SINGLETON);

		// PrimaryMailSender
		bind(Key.get(JavaMailSender.class, PrimaryMailSender.class)).toProvider(new Provider<JavaMailSender>() {

			public JavaMailSender get() {
				final JavaMailSenderImpl impl = new JavaMailSenderImpl();
				impl.setHost(host);
				impl.setUsername(username);
				impl.setPassword(password);
				return impl;
			}

			@Inject
			@Named("mail.host.primary")
			String host;
			@Inject
			@Named("mail.host.primary.username")
			String username;
			@Inject
			@Named("mail.host.primary.password")
			String password;

		}).in(Scopes.SINGLETON);

		// SecondaryMailSender
		bind(Key.get(JavaMailSender.class, SecondaryMailSender.class)).toProvider(new Provider<JavaMailSender>() {

			public JavaMailSender get() {
				final JavaMailSenderImpl impl = new JavaMailSenderImpl();
				impl.setHost(host);
				impl.setUsername(username);
				impl.setPassword(password);
				return impl;
			}

			@Inject
			@Named("mail.host.secondary")
			String host;
			@Inject
			@Named("mail.host.secondary.username")
			String username;
			@Inject
			@Named("mail.host.secondary.password")
			String password;

		}).in(Scopes.SINGLETON);

		// IMailSender
		bind(IMailSender.class).toProvider(new Provider<IMailSender>() {

			public IMailSender get() {
				final List<JavaMailSender> javaMailSenders = Arrays.asList(primary, secondary);

				final List<IComposer<? extends IMailContext>> composers = new ArrayList<IComposer<? extends IMailContext>>(2);
				composers.add(templateComposer);
				composers.add(simpleComposer);

				return new MailSender(javaMailSenders, numberOfSendRetries, sendRetryDelayMilis, composers);
			}

			@Inject
			@PrimaryMailSender
			JavaMailSender primary;
			@Inject
			@SecondaryMailSender
			JavaMailSender secondary;
			@Inject
			@Named("mail.numberOfSendRetries")
			int numberOfSendRetries;
			@Inject
			@Named("mail.sendRetryDelayMilis")
			int sendRetryDelayMilis;
			@Inject
			TemplateComposer templateComposer;
			@Inject
			SimpleComposer simpleComposer;

		}).in(Scopes.SINGLETON);

	}

}
