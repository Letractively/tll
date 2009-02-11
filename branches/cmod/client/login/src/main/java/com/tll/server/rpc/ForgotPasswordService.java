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

import com.tll.common.data.Payload;
import com.tll.common.data.Status;
import com.tll.common.data.rpc.IForgotPasswordService;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.mail.IMailContext;
import com.tll.mail.MailManager;
import com.tll.mail.MailRouting;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.User;
import com.tll.server.AppServletUtil;
import com.tll.server.IAppContext;
import com.tll.server.RequestContext;
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
				final IAppContext ac = rc.getAppContext();
				final IEntityServiceFactory esf = ac.getEntityServiceFactory();
				final IUserService userService = esf.instance(IUserService.class);
				final User user = (User) userService.loadUserByUsername(emailAddress);
				data.put("emailAddress", user.getUsername());
				data.put("password", userService.resetPassword(user.getId()));
				final MailManager mailManager = ac.getMailManager();
				final MailRouting mr = mailManager.buildAppSenderMailRouting(user.getEmailAddress());
				final IMailContext mailContext = mailManager.buildTextTemplateContext(mr, EMAIL_TEMPLATE_NAME, data);
				mailManager.sendEmail(mailContext);
				status.addMsg("Password reminder email was sent.", MsgLevel.INFO);
			}
			catch(final EntityNotFoundException nfe) {
				AppServletUtil.handleException(rc, p.getStatus(), nfe, nfe.getMessage(), false);
			}
			catch(final ChangeUserCredentialsFailedException e) {
				AppServletUtil.handleException(rc, p.getStatus(), e, e.getMessage(), false);
			}
			catch(final MailSendException mse) {
				AppServletUtil.handleException(rc, p.getStatus(), mse, mse.getMessage(), true);
			}
		}

		return p;
	}

}
