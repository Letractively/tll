package com.tll.service.entity.impl.account;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.validator.InvalidStateException;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAccountDao;
import com.tll.dao.impl.IAccountHistoryDao;
import com.tll.dao.impl.IPaymentInfoDao;
import com.tll.model.EntityAssembler;
import com.tll.model.EntityCache;
import com.tll.model.EntityType;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.AccountStatus;
import com.tll.model.key.INameKey;
import com.tll.service.entity.StatefulEntityService;

/**
 * AccountService - {@link IAccountService} impl
 * @author jpk
 */
@Transactional
public class AccountService extends StatefulEntityService<Account, IAccountDao> implements IAccountService {

	private final IPaymentInfoDao paymentInfoDao;

	private final IAccountHistoryDao accountHistoryDao;

	/**
	 * Constructor
	 * @param dao
	 * @param paymentInfoDao
	 * @param accountHistoryDao
	 * @param entityAssembler
	 */
	@Inject
	public AccountService(IAccountDao dao, IPaymentInfoDao paymentInfoDao, IAccountHistoryDao accountHistoryDao,
			EntityAssembler entityAssembler) {
		super(IAccountDao.class, dao, entityAssembler);
		this.paymentInfoDao = paymentInfoDao;
		this.accountHistoryDao = accountHistoryDao;
	}

	@Override
	public Class<Account> getEntityClass() {
		return Account.class;
	}

	public Account load(INameKey<? extends Account> key) throws EntityNotFoundException {
		return dao.load(key);
	}

	@Override
	public void deleteAll(Collection<Account> entities) {
		super.deleteAll(entities);
		if(entities != null && entities.size() > 0) {
			for(Account a : entities) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_DELETED, a));
			}
		}
	}

	@Override
	public Collection<Account> persistAll(Collection<Account> entities) {
		Collection<Account> pec = super.persistAll(entities);
		if(pec != null && pec.size() > 0) {
			for(Account a : pec) {
				AccountHistoryOp op = a.isNew() ? AccountHistoryOp.ACCOUNT_ADDED : AccountHistoryOp.ACCOUNT_UPDATED;
				addHistoryRecord(new AccountHistoryContext(op, a));
			}
		}
		return pec;
	}

	@Override
	public Account persist(Account entity) throws EntityExistsException, InvalidStateException {
		Account pe = super.persist(entity);

		// persist payment info?
		if(entity.getPersistPymntInfo() && entity.getPaymentInfo() != null) {
			paymentInfoDao.persist(entity.getPaymentInfo());
		}

		if(pe != null) {
			AccountHistoryOp op = entity.isNew() ? AccountHistoryOp.ACCOUNT_ADDED : AccountHistoryOp.ACCOUNT_UPDATED;
			addHistoryRecord(new AccountHistoryContext(op, pe));
		}
		return pe;
	}

	@Override
	public void purgeAll(Collection<Account> entities) {
		super.purgeAll(entities);
		if(entities != null && entities.size() > 0) {
			for(Account a : entities) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_PURGED, a));
			}
		}
	}

	@Override
	public void purge(Account entity) {
		super.purge(entity);
		addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_PURGED, entity));
	}

	public void delete(Account e) {
		e.setStatus(AccountStatus.CLOSED);
		super.persist(e);
		addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_DELETED, e));
	}

	/**
	 * Adds an account history record
	 * @param context
	 * @throws EntityExistsException
	 */
	private void addHistoryRecord(AccountHistoryContext context) {
		switch(context.getOp()) {
			// add account
			case ACCOUNT_ADDED: {
				AccountHistory ah =
						entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(context.getAccount()), true);
				ah.setNotes(context.getAccount().typeName() + " created");
				ah.setStatus(AccountStatus.NEW);
				accountHistoryDao.persist(ah);
				break;
			}
				// delete account
			case ACCOUNT_DELETED: {
				AccountHistory ah =
						entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(context.getAccount()), true);
				ah.setStatus(AccountStatus.DELETED);
				ah.setNotes(context.getAccount().typeName() + " marked as DELETED");
				accountHistoryDao.persist(ah);
				break;
			}
				// purge account
			case ACCOUNT_PURGED: {
				// add history record to parentAccount
				Account parent = context.getAccount().getParent();
				if(parent != null) {
					AccountHistory ah = entityAssembler.assembleEntity(EntityType.ACCOUNT_HISTORY, new EntityCache(parent), true);
					ah.setStatus(AccountStatus.DELETED);
					ah.setNotes("Child account: " + context.getAccount().typeName() + "'" + context.getAccount().descriptor()
							+ "' DELETED");
					accountHistoryDao.persist(ah);
				}
				break;
			}

		}// switch

	}
}
