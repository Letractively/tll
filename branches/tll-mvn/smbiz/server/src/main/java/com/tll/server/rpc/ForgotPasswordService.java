/**
 * The Logic Lab
 * @author jpk
 * Aug 25, 2007
 */
package com.tll.server.rpc;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.mail.MailSendException;

import com.tll.client.data.Payload;
import com.tll.client.data.Status;
import com.tll.client.data.rpc.IForgotPasswordService;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.mail.IMailContext;
import com.tll.mail.MailManager;
import com.tll.mail.MailRouting;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.User;
import com.tll.server.RequestContext;
import com.tll.server.ServletUtil;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.user.IUserService;
import com.tll.util.StringUtil;

/**
 * ForgotPasswordService
 * @author jpk
 */
public class ForgotPasswordService extends RpcServlet implements IForgotPasswordService {

	private static final long serialVersionUID = 1144692563596509841L;
	private static final String EMAIL_TEMPLATE_NAME = "forgot-password";

	public Payload requestPassword(final String emailAddress) {
		final Status status = new Status();
		final Payload p = new Payload(status);
		final Map<String, Object> data = new HashMap<String, Object>();

		if(StringUtil.isEmpty(emailAddress)) {
			status.addMsg("An email address must be specified.", MsgLevel.ERROR);
		}
		else {
			final RequestContext rc = getRequestContext();
			try {
				final IEntityServiceFactory esf = rc.getEntityServiceFactory();
				final IUserService userService = esf.instance(IUserService.class);
				final User user = (User) userService.loadUserByUsername(emailAddress);
				data.put("emailAddress", user.getUsername());
				data.put("password", userService.resetPassword(user.getId()));
				final MailManager mailManager = rc.getMailManager();
				final MailRouting mr = mailManager.buildAppSenderMailRouting(user.getEmailAddress());
				final IMailContext mailContext = mailManager.buildTextTemplateContext(mr, EMAIL_TEMPLATE_NAME, data);
				mailManager.sendEmail(mailContext);
				status.addMsg("Password reminder email was sent.", MsgLevel.INFO);
			}
			catch(final EntityNotFoundException nfe) {
				ServletUtil.handleException(rc, p.getStatus(), nfe, nfe.getMessage(), false);
			}
			catch(final ChangeUserCredentialsFailedException e) {
				ServletUtil.handleException(rc, p.getStatus(), e, e.getMessage(), false);
			}
			catch(final MailSendException mse) {
				ServletUtil.handleException(rc, p.getStatus(), mse, mse.getMessage(), true);
			}
		}

		return p;
	}

}
