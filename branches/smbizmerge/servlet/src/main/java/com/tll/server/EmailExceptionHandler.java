/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.tll.mail.MailManager;
import com.tll.mail.NameEmail;
import com.tll.server.EmailExceptionHandlerModule.OnErrorEmail;

/**
 * Emails exception notification emails.
 * @author jpk
 */
public class EmailExceptionHandler implements IExceptionHandler {
	private final MailManager mailManager;

	private final NameEmail onErrorEmail;

	/**
	 * Constructor
	 * @param mailManager
	 * @param onErrorEmail
	 */
	@Inject
	public EmailExceptionHandler(MailManager mailManager, @OnErrorEmail NameEmail onErrorEmail) {
		super();
		if(mailManager == null) throw new IllegalArgumentException("No mail manager specified.");
		if(onErrorEmail == null) throw new IllegalArgumentException("No on error email address specified.");
		this.mailManager = mailManager;
		this.onErrorEmail = onErrorEmail;
	}

	/**
	 * Used for doling out exception notification emails.
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

	@Override
	public void handleException(final Throwable t) {
		final Map<String, Object> data = new HashMap<String, Object>();
		data.put("header", "Exception Notification (" + t.getClass().getSimpleName() + ")");
		synchronized(this) {
			data.put("datetime", sdf.format(new Date()));
		}
		String emsg = t.getMessage();
		if(emsg == null) {
			emsg = t.getClass().getSimpleName();
		}
		data.put("error", emsg);
		final StackTraceElement ste =
			(t.getStackTrace() == null || t.getStackTrace().length < 1) ? null : t.getStackTrace()[0];
		data.put("trace", ste == null ? "[NO STACK TRACE]" : ste.toString());
		mailManager.sendEmail(mailManager.buildTextTemplateContext(mailManager.buildAppSenderMailRouting(onErrorEmail),
				"exception-notification", data));
	}
}
