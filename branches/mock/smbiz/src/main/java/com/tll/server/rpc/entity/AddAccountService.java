/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.server.rpc.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.tll.common.data.AddAccountRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.Status;
import com.tll.common.data.rpc.IAddAccountService;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.model.Account;
import com.tll.model.AccountInterface;
import com.tll.model.Customer;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.User;
import com.tll.server.AppContext;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.RpcServlet;


/**
 * AddAccountService
 * @author jpk
 */
public class AddAccountService extends RpcServlet implements IAddAccountService {
	private static final long serialVersionUID = 2363373679538578191L;

	@SuppressWarnings("unchecked")
	@Override
	public ModelPayload add(AddAccountRequest request) {
		final ModelPayload p = new ModelPayload();
		final Status s = p.getStatus();

		final AppContext ac = (AppContext) getServletContext().getAttribute(AppContext.KEY);
		assert ac != null;
		final com.tll.service.entity.account.AddAccountService svc = ac.getAddAccountService();
		final PersistContext pc = (PersistContext) getServletContext().getAttribute(PersistContext.KEY);
		assert svc != null && pc != null;
		final Marshaler mlr = pc.getMarshaler();

		Class<? extends Account> accountClass;
		try {
			accountClass = (Class<? extends Account>) pc.getEntityTypeResolver().resolveEntityClass(request.getEntityType());
		}
		catch(final ClassCastException e) {
			s.addMsg("Invalid account type.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			return p;
		}
		Model maccount = request.getAccount();
		if(maccount == null) {
			s.addMsg("No account specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			return p;
		}
		final Collection<Model> maios = request.getAccountInterfaces();
		if(maios == null) {
			s.addMsg("No account interface options specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
			return p;
		}
		final Collection<Model> musers = request.getUsers();

		try {
			// un-marshal
			Account account = mlr.unmarshalEntity(accountClass, maccount);

			final ArrayList<AccountInterface> aios = new ArrayList<AccountInterface>(maios.size());
			for(final Model maio : maios) {
				aios.add(mlr.unmarshalEntity(AccountInterface.class, maio));
			}

			final ArrayList<User> users = musers == null ? null : new ArrayList<User>(musers.size());
			if(musers != null) {
				for(final Model muser : musers) {
					users.add(mlr.unmarshalEntity(User.class, muser));
				}
			}

			if(Isp.class == accountClass) {
				account = svc.addIsp((Isp) account, aios, users);
				s.addMsg("Isp added", MsgLevel.INFO, MsgAttr.STATUS.flag);
			}
			else if(Merchant.class == accountClass) {
				account = svc.addMerchant((Merchant) account, aios, users);
				s.addMsg("Merchant added", MsgLevel.INFO, MsgAttr.STATUS.flag);
			}
			else if(Customer.class == accountClass) {
				account = svc.addCustomer((Customer) account, aios, users);
				s.addMsg("Customer added", MsgLevel.INFO, MsgAttr.STATUS.flag);
			}
			else {
				s.addMsg("Unhandled account type: " + accountClass, MsgLevel.ERROR, MsgAttr.STATUS.flag);
				return p;
			}

			// marshal the added account
			maccount = mlr.marshalEntity(account, pc.getMarshalOptionsResolver().resolve(SmbizEntityType.ACCOUNT));
			p.setModel(maccount);

			return p;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, s);
			pc.getExceptionHandler().handleException(e);
			throw e;
		}
	}

}
