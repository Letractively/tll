package com.tll.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * The mail sender.
 * @author jpk
 */
public class MailSender implements IMailSender {

	private static final Logger log = LoggerFactory.getLogger(MailSender.class);

	public static final int DEFAULT_NUMBER_OF_SEND_RETRIES = 0;

	public static final int DEFAULT_SEND_RETRY_DELAY_MILIS = 1000;

	/**
	 * List of {@link JavaMailSender} objects. The order dictates the try priority
	 * of the mail sender.
	 */
	private final List<JavaMailSender> javaMailSenders;

	/**
	 * The number of times to re-try sending an email.
	 */
	private int numberOfSendRetries = DEFAULT_NUMBER_OF_SEND_RETRIES;

	/**
	 * The delay in mili-seconds to wait until re-trying to send an email.
	 */
	private int sendRetryDelayMilis = DEFAULT_SEND_RETRY_DELAY_MILIS;

	/**
	 * List of supported email composers.
	 */
	private List<IComposer<? extends IMailContext>> composers = new ArrayList<IComposer<? extends IMailContext>>();

	/**
	 * Constructor
	 * @param javaMailSenders
	 * @param numberOfSendRetries
	 * @param sendRetryDelayMilis
	 * @param composers
	 */
	public MailSender(List<JavaMailSender> javaMailSenders, int numberOfSendRetries, int sendRetryDelayMilis,
			List<IComposer<? extends IMailContext>> composers) {
		super();
		this.javaMailSenders = javaMailSenders;
		this.numberOfSendRetries = numberOfSendRetries;
		this.sendRetryDelayMilis = sendRetryDelayMilis;
		this.composers = composers;
	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private <C extends IMailContext> IComposer<C> getComposerFromContext(C context) {
		for(final IComposer c : composers) {
			if(c.supports(context.getClass())) {
				return c;
			}
		}
		throw new IllegalArgumentException("Unsupported mail context type: " + context.getClass().getSimpleName());
	}

	@Override
	public <C extends IMailContext> void send(C context) throws MailSendException {
		final IComposer<C> composer = getComposerFromContext(context);
		int retries;

		for(final JavaMailSender sender : javaMailSenders) {
			retries = 0;// reset
			final MimeMessage mimeMessage = sender.createMimeMessage();
			composer.compose(mimeMessage, context);

			while(retries++ < numberOfSendRetries) {
				// send the email
				try {
					log.debug("Sending email: {}...", context.getName());
					sender.send(mimeMessage);
					log.info(context.getName() + " Message sent.");
					return;
				}
				catch(final MailAuthenticationException mae) {
					log.error("Failed email delivery attempt: " + mae.getMessage());
					break;// no retries for this exception
				}
				catch(final MailSendException mse) {
					log.error("Failed email delivery attempt: " + mse.getMessage());
					try {
						Thread.sleep(sendRetryDelayMilis);
					}
					catch(final InterruptedException ie) {
						throw new IllegalStateException("Thread [MailSender] was interrupted!", ie);
					}
				}
			}
		}

		log.error("Email delivery for '" + context.getName() + "' FAILED");
		throw new MailSendException("Failed email delivery attempt: " + context.getName());
	}

}
