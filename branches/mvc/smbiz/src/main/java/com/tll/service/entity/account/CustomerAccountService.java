package com.tll.service.entity.account;

import java.util.Collection;

import javax.persistence.EntityExistsException;

import org.hibernate.validator.InvalidStateException;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AccountHistory;
import com.tll.model.AccountStatus;
import com.tll.model.CustomerAccount;
import com.tll.model.EntityCache;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.EntityService;
import com.tll.service.entity.account.AccountHistoryContext.AccountHistoryOp;

/**
 * CustomerAccountService - {@link ICustomerAccountService} impl
 * @author jpk
 */
@Transactional
public class CustomerAccountService extends EntityService<CustomerAccount> implements ICustomerAccountService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public CustomerAccountService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<CustomerAccount> getEntityClass() {
		return CustomerAccount.class;
	}

	@Override
	public void deleteAll(Collection<CustomerAccount> entities) {
		if(entities != null && entities.size() > 0) {
			for(CustomerAccount e : entities) {
				delete(e);
			}
		}
		if(entities != null && entities.size() > 0) {
			for(CustomerAccount e : entities) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_DELETED, e));
			}
		}
	}

	@Override
	public Collection<CustomerAccount> persistAll(Collection<CustomerAccount> entities) {
		Collection<CustomerAccount> pec = super.persistAll(entities);
		if(pec != null && pec.size() > 0) {
			for(CustomerAccount e : pec) {
				if(e.isNew()) {
					addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.CUSTOMER_ACCOUNT_ADDED, e));
				}
			}
		}
		return pec;
	}

	@Override
	public CustomerAccount persist(CustomerAccount entity) throws EntityExistsException, InvalidStateException {
		CustomerAccount pe = super.persist(entity);
		if(pe != null) {
			if(entity.isNew()) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.CUSTOMER_ACCOUNT_ADDED, pe));
			}
		}
		return pe;
	}

	@Override
	public void purgeAll(Collection<CustomerAccount> entities) {
		super.purgeAll(entities);
		if(entities != null && entities.size() > 0) {
			for(CustomerAccount e : entities) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.CUSTOMER_ACCOUNT_PURGED, e));
			}
		}
	}

	@Override
	public void purge(CustomerAccount entity) {
		super.purge(entity);
		addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.CUSTOMER_ACCOUNT_PURGED, entity));
	}

	public void delete(CustomerAccount e) {
		e.setStatus(AccountStatus.DELETED);
		super.persist(e);
		addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.CUSTOMER_ACCOUNT_DELETED, e));
	}

	/**
	 * Adds an account history record
	 * @param context
	 * @throws EntityExistsException
	 */
	private void addHistoryRecord(AccountHistoryContext context) {
		switch(context.getOp()) {

			// add customer account
			case CUSTOMER_ACCOUNT_ADDED: {
				AccountHistory ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getCustomer().getStatus());
				ah.setNotes(context.getCustomerAccount().getCustomer().descriptor() + " bound");
				dao.persist(ah);

				ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getAccount().getStatus());
				ah.setNotes("bound to account: " + context.getCustomerAccount().getAccount().descriptor());
				dao.persist(ah);
				break;
			}

				// purge customer account
			case CUSTOMER_ACCOUNT_PURGED: {
				AccountHistory ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getCustomer().getStatus());
				ah.setNotes(context.getCustomerAccount().getCustomer().descriptor() + " un-bound (removed)");
				dao.persist(ah);

				ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getAccount().getStatus());
				ah.setNotes("un-bound from account: " + context.getCustomerAccount().getAccount().descriptor());
				dao.persist(ah);
				break;
			}

		}// switch

	}
}
