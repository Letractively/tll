/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.RefKey;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.Comparator;
import com.tll.criteria.ICriteria;
import com.tll.model.Account;
import com.tll.model.AccountStatus;
import com.tll.model.SmbizEntityType;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.server.marshal.MarshalOptions;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.account.IAccountService;
import com.tll.util.EnumUtil;
public class AccountService extends MNamedEntityServiceImpl<Account, AccountSearch> {

	private static final MarshalOptions marshalOptions = new MarshalOptions(true, 2);

	public MarshalOptions getMarshalOptions(IMEntityServiceContext context) {
		return marshalOptions;
	}

	@Override
	protected void handleLoadOptions(IMEntityServiceContext context, Account e, EntityOptions entityOptions,
			Map<String, RefKey> refs) throws SystemError {

		IEntityServiceFactory entityServiceFactory = context.getEntityServiceFactory();

		// THIS is taken care of via open session in view filter and auto-proxy
		// loading in hibernate

		// load related addresses?
		/*
		if(entityOptions.isRelatedRequested(IEntityType.ACCOUNT_ADDRESS)) {
			IPrimaryKey<Account> pk = KeyFactory.getPrimaryKey(Account.class, e.getId());
			ICriteria<? extends AccountAddress> c = CriteriaFactory.buildForeignKeyCriteria(AccountAddress.class, pk);
			IAccountAddressService aas = entityServiceFactory.instance(IAccountAddressService.class);
			try {
				e.addAccountAddresses(aas.findByCriteria(c));
			}
			catch(InvalidCriteriaException e1) {
				throw new SystemError("Invalid account addresses criteria: " + e1.getMessage(), e1);
			}
			catch(Throwable t) {
				throw new SystemError(t.getMessage(), t);
			}
		}
		
		// load payment info?
		if(entityOptions.isRelatedRequested(IEntityType.PAYMENT_INFO) && e.getPaymentInfo() != null) {
			IPaymentInfoService pis = entityServiceFactory.instance(IPaymentInfoService.class);
			IPrimaryKey<PaymentInfo> pk = KeyFactory.getPrimaryKey(PaymentInfo.class, e.getPaymentInfo().getId());
			PaymentInfo pi = pis.load(pk);
			e.setPaymentInfo(pi);
		}
		*/

		// load parent account ref?
		if(entityOptions.isRelatedRefRequested(SmbizEntityType.ACCOUNT) && e.getParent() != null) {
			PrimaryKey<Account> pk = new PrimaryKey<Account>(Account.class, e.getParent().getId());
			IAccountService svc = entityServiceFactory.instance(IAccountService.class);
			Account parent = svc.load(pk);
			RefKey er = new RefKey(SmbizEntityType.ACCOUNT, parent.getId(), parent.getName());
			refs.put("parent", er);
		}
	}

	@Override
	protected void handlePersistOptions(IMEntityServiceContext context, Account e, EntityOptions options)
			throws SystemError {
		// no-op
	}

	@Override
	protected IBusinessKey<Account> handleBusinessKeyTranslation(AccountSearch search) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	protected void handleSearchTranslation(IMEntityServiceContext context, AccountSearch search,
			ICriteria<Account> criteria) {

		// date ranges
		criteria.getPrimaryGroup().addCriterion("dateCreated", search.getDateCreatedRange());
		criteria.getPrimaryGroup().addCriterion("dateModified", search.getDateModifiedRange());

		// name
		criteria.getPrimaryGroup().addCriterion("name", search.getName(), Comparator.EQUALS, false);

		// parent account ref
		RefKey par = search.getParentAccountRef();
		if(par != null) {
			criteria.getPrimaryGroup().addCriterion("parent", new PrimaryKey<Account>(Account.class, par.getId()));
		}

		// status
		String status = search.getStatus();
		if(status != null) {
			criteria.getPrimaryGroup().addCriterion("status", EnumUtil.fromString(AccountStatus.class, status));
		}
	}
}
