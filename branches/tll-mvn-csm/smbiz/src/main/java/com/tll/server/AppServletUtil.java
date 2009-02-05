/**
 * The Logic Lab
 * @author jpk
 * Feb 4, 2009
 */
package com.tll.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.MailSendException;

import com.tll.common.data.Status;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.config.Config;
import com.tll.mail.MailManager;
import com.tll.mail.NameEmail;


/**
 * AppServletUtil
 * @author jpk
 */
public class AppServletUtil {

	/**
	 * Used for doling out exception notification emails.
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

	/**
	 * Unified way to handle exceptions for an RPC call.
	 * @param requestContext The request context.
	 * @param status The Status that will be sent to the client.
	 * @param t The exception.
	 * @param uiDisplayText The text to display to the user in the UI. May be
	 *        <code>null</code> in which case, the message will not be displayed
	 *        in the UI.
	 * @param emailException Whether or not a notification email is sent
	 *        containing the exception stack trace etc.
	 */
	public static void handleException(final RequestContext requestContext, final Status status, final Throwable t,
			final String uiDisplayText, final boolean emailException) {
		assert status != null && t != null;
		String emsg = t.getMessage();
		if(emsg == null) {
			emsg = t.getClass().getSimpleName();
		}
		assert emsg != null;
		if(t instanceof RuntimeException) {
			status.addMsg(emsg, MsgLevel.FATAL, MsgAttr.EXCEPTION.flag | MsgAttr.NODISPLAY.flag);
			if(uiDisplayText != null) {
				status.addMsg(uiDisplayText, MsgLevel.FATAL, MsgAttr.EXCEPTION.flag);
			}
		}
		else {
			status.addMsg(emsg, MsgLevel.ERROR, MsgAttr.EXCEPTION.flag | MsgAttr.NODISPLAY.flag);
			if(uiDisplayText != null) {
				status.addMsg(uiDisplayText, MsgLevel.ERROR, MsgAttr.EXCEPTION.flag);
			}
		}
		if(emailException) {
			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("header", "Exception Notification (" + t.getClass().getSimpleName() + ")");
			data.put("datetime", sdf.format(new Date()));
			data.put("error", emsg);
			final StackTraceElement ste =
					(t.getStackTrace() == null || t.getStackTrace().length < 1) ? null : t.getStackTrace()[0];
			data.put("trace", ste == null ? "[NO STACK TRACE]" : ste.toString());
			try {
				final MailManager mailManager = requestContext.getMailManager();
				final String onErrorEmail = Config.instance().getString("mail.onerror.ToAddress");
				final String onErrorName = Config.instance().getString("mail.onerror.ToName");
				final NameEmail ne = new NameEmail(onErrorName, onErrorEmail);
				mailManager.sendEmail(mailManager.buildTextTemplateContext(mailManager.buildAppSenderMailRouting(ne),
						"exception-notification", data));
			}
			catch(final MailSendException mse) {
				status.addMsg("Unable to send exception notification email: " + mse.getMessage(), MsgLevel.ERROR,
						MsgAttr.NODISPLAY.flag);
			}
		}
	}
}
