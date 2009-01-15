package com.tll.mail;

import org.springframework.mail.MailSendException;

/**
 * Provision to send email.
 * @author jpk
 */
public interface IMailSender {

	/**
	 * @param context The send mail context holding necessary parameters to send
	 *        the email.
	 */
	public <C extends IMailContext> void send(C context) throws MailSendException;
}
