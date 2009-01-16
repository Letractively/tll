package com.tll.service.entity.impl.account;

import java.util.Collection;

import javax.persistence.EntityExistsException;

import org.hibernate.validator.InvalidStateException;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAccountHistoryDao;
import com.tll.dao.impl.ICustomerAccountDao;
import com.tll.model.EntityAssembler;
import com.tll.model.EntityCache;
import com.tll.model.EntityType;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.AccountStatus;
import com.tll.model.impl.CustomerAccount;
import com.tll.service.entity.StatefulEntityService;

/**
 * CustomerAccountService - {@link ICustomerAccountService} impl
 * @author jpk
 */
@Transactional
public class CustomerAccountService extends StatefulEntityService<CustomerAccount, ICustomerAccountDao> implements ICustomerAccountService {

	private final IAccountHistoryDao accountHistoryDao;

	/**
	 * Constructor
	 * @param dao
	 * @param accountHistoryDao
	 * @param entityAssembler
	 */
	@Inject
	public CustomerAccountService(ICustomerAccountDao dao, IAccountHistoryDao accountHistoryDao,
			EntityAssembler entityAssembler) {
		super(ICustomerAccountDao.class, dao, entityAssembler);
		this.accountHistoryDao = accountHistoryDao;
	}

	@Override
	public Class<CustomerAccount> getEntityClass() {
		return CustomerAccount.class;
	}

	@Override
	public void deleteAll(Collection<CustomerAccount> entities) {
		super.deleteAll(entities);
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
						entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getCustomer().getStatus());
				ah.setNotes(context.getCustomerAccount().getCustomer().descriptor() + " bound");
				accountHistoryDao.persist(ah);

				ah =
						entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getAccount().getStatus());
				ah.setNotes("bound to account: " + context.getCustomerAccount().getAccount().descriptor());
				accountHistoryDao.persist(ah);
				break;
			}

				// purge customer account
			case CUSTOMER_ACCOUNT_PURGED: {
				AccountHistory ah =
						entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getCustomer().getStatus());
				ah.setNotes(context.getCustomerAccount().getCustomer().descriptor() + " un-bound (removed)");
				accountHistoryDao.persist(ah);

				ah =
						entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(context.getCustomerAccount()
								.getAccount()), true);
				ah.setStatus(context.getCustomerAccount().getAccount().getStatus());
				ah.setNotes("un-bound from account: " + context.getCustomerAccount().getAccount().descriptor());
				accountHistoryDao.persist(ah);
				break;
			}

		}// switch

	}
}
