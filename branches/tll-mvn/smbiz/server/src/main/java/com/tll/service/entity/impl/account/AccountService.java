package com.tll.service.entity.impl.account;

import java.util.Collection;

import javax.persistence.EntityExistsException;

import org.hibernate.validator.InvalidStateException;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.AccountHistory;
import com.tll.model.AccountStatus;
import com.tll.model.EntityCache;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;

/**
 * AccountService - {@link IAccountService} impl
 * @author jpk
 */
@Transactional
public class AccountService extends NamedEntityService<Account> implements IAccountService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AccountService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Account> getEntityClass() {
		return Account.class;
	}

	@Override
	public void deleteAll(Collection<Account> entities) {
		if(entities != null && entities.size() > 0) {
			for(Account e : entities) {
				delete(e);
			}
		}
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
			dao.persist(entity.getPaymentInfo());
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
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getAccount()), true);
				ah.setNotes(context.getAccount().typeName() + " created");
				ah.setStatus(AccountStatus.NEW);
				dao.persist(ah);
				break;
			}
				// delete account
			case ACCOUNT_DELETED: {
				AccountHistory ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getAccount()), true);
				ah.setStatus(AccountStatus.DELETED);
				ah.setNotes(context.getAccount().typeName() + " marked as DELETED");
				dao.persist(ah);
				break;
			}
				// purge account
			case ACCOUNT_PURGED: {
				// add history record to parentAccount
				Account parent = context.getAccount().getParent();
				if(parent != null) {
					AccountHistory ah = entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(parent), true);
					ah.setStatus(AccountStatus.DELETED);
					ah.setNotes("Child account: " + context.getAccount().typeName() + "'" + context.getAccount().descriptor()
							+ "' DELETED");
					dao.persist(ah);
				}
				break;
			}

		}// switch

	}
}
