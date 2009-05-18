/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.util.Map;

import com.tll.SystemError;
import com.tll.common.data.EntityOptions;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountSearch;
import com.tll.common.search.ISearch;
import com.tll.criteria.Comparator;
import com.tll.criteria.ICriteria;
import com.tll.model.Account;
import com.tll.model.AccountStatus;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.server.marshal.MarshalOptions;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.INamedEntityService;
import com.tll.service.entity.account.IAccountService;
import com.tll.util.EnumUtil;

public class AccountService extends PersistServiceImpl<Account> {

	public static final MarshalOptions MARSHAL_OPTIONS = new MarshalOptions(true, 2);

	public MarshalOptions getMarshalOptions(PersistContext context) {
		return MARSHAL_OPTIONS;
	}

	@Override
	protected Account loadByName(Class<Account> entityClass, IEntityService<Account> svc, String name)
			throws UnsupportedOperationException {
		final INamedEntityService<Account> nsvc = (INamedEntityService<Account>) svc;
		return nsvc.load(new NameKey<Account>(entityClass, name));
	}

	@Override
	protected void handleLoadOptions(PersistContext context, Account e, EntityOptions entityOptions,
			Map<String, ModelKey> refs) throws SystemError {

		final IEntityServiceFactory entityServiceFactory = context.getEntityServiceFactory();

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
			final PrimaryKey<Account> pk = new PrimaryKey<Account>(Account.class, e.getParent().getId());
			final IAccountService svc = entityServiceFactory.instance(IAccountService.class);
			final Account parent = svc.load(pk);
			final ModelKey er = new ModelKey(SmbizEntityType.ACCOUNT, parent.getId(), parent.getName());
			refs.put("parent", er);
		}
	}

	@Override
	protected void handlePersistOptions(PersistContext context, Account e, EntityOptions options)
	throws SystemError {
		// no-op
	}

	@Override
	protected IBusinessKey<Account> handleBusinessKeyTranslation(ISearch search) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@Override
	protected void handleSearchTranslation(PersistContext context, ISearch search,
			ICriteria<Account> criteria) {

		if(search instanceof AccountSearch) {
			final AccountSearch as = (AccountSearch) search;

			// date ranges
			criteria.getPrimaryGroup().addCriterion("dateCreated", as.getDateCreatedRange());
			criteria.getPrimaryGroup().addCriterion("dateModified", as.getDateModifiedRange());

			// name
			criteria.getPrimaryGroup().addCriterion("name", as.getName(), Comparator.EQUALS, false);

			// parent account ref
			final ModelKey par = as.getParentAccountRef();
			if(par != null) {
				criteria.getPrimaryGroup().addCriterion("parent", new PrimaryKey<Account>(Account.class, par.getId()));
			}

			// status
			final String status = as.getStatus();
			if(status != null) {
				criteria.getPrimaryGroup().addCriterion("status", EnumUtil.fromString(AccountStatus.class, status));
			}
		}
		else {
			throw new IllegalArgumentException("Unhandled account search type");
		}
	}
}
