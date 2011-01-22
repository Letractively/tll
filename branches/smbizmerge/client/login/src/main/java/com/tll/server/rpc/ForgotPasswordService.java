/**
 * The Logic Lab
 * @author jpk
 * Aug 25, 2007
 */
package com.tll.server.rpc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.MailSendException;

import com.tll.common.data.Payload;
import com.tll.common.data.rpc.IForgotPasswordService;
import com.tll.common.msg.Status;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.dao.EntityNotFoundException;
import com.tll.mail.IMailContext;
import com.tll.mail.MailManager;
import com.tll.mail.MailRouting;
import com.tll.model.IUserRef;
import com.tll.service.ChangeUserCredentialsFailedException;
import com.tll.service.IForgotPasswordHandler;
import com.tll.util.StringUtil;

/**
 * ForgotPasswordService
 * @author jpk
 */
public class ForgotPasswordService extends RpcServlet implements IForgotPasswordService {

	private static final long serialVersionUID = 1144692563596509841L;
	private static final String EMAIL_TEMPLATE_NAME = "forgot-password";

	private ForgotPasswordServiceContext getContext() {
		return (ForgotPasswordServiceContext) getThreadLocalRequest().getSession(false).getServletContext().getAttribute(
				ForgotPasswordServiceContext.KEY);
	}

	@Override
	public Payload requestPassword(final String emailAddress) {
		final Status status = new Status();
		final Payload p = new Payload(status);
		final Map<String, Object> data = new HashMap<String, Object>();

		if(StringUtil.isEmpty(emailAddress)) {
			status.addMsg("An email address must be specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}
		else {
			final ForgotPasswordServiceContext context = getContext();
			try {
				final IForgotPasswordHandler handler = context.getForgotPasswordHandler();
				final IUserRef user = handler.getUserRef(emailAddress);
				final String rp = handler.resetPassword(user.getId());
				data.put("username", user.getUsername());
				data.put("emailAddress", user.getEmailAddress());
				data.put("password", rp);
				final MailManager mailManager = context.getMailManager();
				final MailRouting mr = mailManager.buildAppSenderMailRouting(user.getEmailAddress());
				final IMailContext mailContext = mailManager.buildTextTemplateContext(mr, EMAIL_TEMPLATE_NAME, data);
				mailManager.sendEmail(mailContext);
				status.addMsg("Password reset email was sent.", MsgLevel.INFO, MsgAttr.STATUS.flag);
			}
			catch(final EntityNotFoundException nfe) {
				exceptionToStatus(nfe, status);
				context.getExceptionHandler().handleException(nfe);
			}
			catch(final ChangeUserCredentialsFailedException e) {
				exceptionToStatus(e, status);
				context.getExceptionHandler().handleException(e);
			}
			catch(final MailSendException mse) {
				exceptionToStatus(mse, status);
				context.getExceptionHandler().handleException(mse);
			}
		}

		return p;
	}

}
