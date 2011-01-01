package com.tll.service.entity.account;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityExistsException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.listhandler.IListingDataProvider;
import com.tll.model.Account;
import com.tll.model.AccountHistory;
import com.tll.model.AccountStatus;
import com.tll.model.EntityCache;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;
import com.tll.service.entity.account.AccountHistoryContext.AccountHistoryOp;

/**
 * AccountService - {@link IAccountService} impl
 * @author jpk
 */
public class AccountService extends NamedEntityService<Account> implements IAccountService {

	/**
	 * AccountHistoryDataProvider
	 * @author jpk
	 */
	@Transactional(readOnly = true)
	private static final class AccountHistoryDataProvider implements IListingDataProvider<AccountHistory> {

		private final IEntityDao dao;

		/**
		 * Constructor
		 * @param dao
		 */
		public AccountHistoryDataProvider(final IEntityDao dao) {
			super();
			this.dao = dao;
		}

		@Override
		public List<SearchResult> find(final Criteria<AccountHistory> criteria, final Sorting sorting) throws InvalidCriteriaException {
			return dao.find(criteria, sorting);
		}

		@Override
		public List<AccountHistory> getEntitiesFromIds(final Class<AccountHistory> entityClass, final Collection<Long> ids, final Sorting sorting) {
			return dao.findByPrimaryKeys(entityClass, ids, sorting);
		}

		@Override
		public List<Long> getPrimaryKeys(final Criteria<AccountHistory> criteria, final Sorting sorting) throws InvalidCriteriaException {
			return dao.getPrimaryKeys(criteria, sorting);
		}

		@Override
		public IPageResult<SearchResult> getPage(final Criteria<AccountHistory> criteria, final Sorting sorting, final int offset,
				final int pageSize) throws InvalidCriteriaException {
			return dao.getPage(criteria, sorting, offset, pageSize);
		}
	}

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public AccountService(final IEntityDao dao, final IEntityAssembler entityAssembler, final ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Account> getEntityClass() {
		return Account.class;
	}

	@Override
	@Transactional
	public void deleteAll(final Collection<Account> entities) {
		if(entities != null && entities.size() > 0) {
			for(final Account e : entities) {
				delete(e);
			}
		}
		if(entities != null && entities.size() > 0) {
			for(final Account a : entities) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_DELETED, a));
			}
		}
	}

	@Override
	@Transactional
	public Collection<Account> persistAll(final Collection<Account> entities) {
		HashMap<Account, Boolean> newmap = new HashMap<Account, Boolean>(entities.size());
		for(Account a : entities) {
			newmap.put(a, a.isNew()? Boolean.TRUE : Boolean.FALSE);
		}
		final Collection<Account> pec = super.persistAll(entities);
		if(pec != null && pec.size() > 0) {
			for(final Account a : pec) {
				final AccountHistoryOp op = newmap.get(a).booleanValue()? AccountHistoryOp.ACCOUNT_ADDED : AccountHistoryOp.ACCOUNT_UPDATED;
				addHistoryRecord(new AccountHistoryContext(op, a));
			}
		}
		return pec;
	}

	@Override
	@Transactional
	public Account persist(final Account entity) throws EntityExistsException, ConstraintViolationException {

		final boolean isNew = entity.isNew();
		
		// handle payment info
		if(entity.getPaymentInfo() != null) {
			if(entity.getPersistPymntInfo()) {
				// persist it
				dao.persist(entity.getPaymentInfo());
			}
			else {
				// kill it
				dao.purge(entity.getPaymentInfo());
			}
		}

		final Account pe = super.persist(entity);

		// handle account history
		if(pe != null) {
			final AccountHistoryOp op = isNew ? AccountHistoryOp.ACCOUNT_ADDED : AccountHistoryOp.ACCOUNT_UPDATED;
			addHistoryRecord(new AccountHistoryContext(op, pe));
		}

		return pe;
	}

	@Override
	@Transactional
	public void purgeAll(final Collection<Account> entities) {
		super.purgeAll(entities);
		if(entities != null && entities.size() > 0) {
			for(final Account a : entities) {
				addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_PURGED, a));
			}
		}
	}

	@Override
	@Transactional
	public void purge(final Account entity) {
		super.purge(entity);
		addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_PURGED, entity));
	}

	@Override
	@Transactional
	public void delete(final Account e) {
		e.setStatus(AccountStatus.CLOSED);
		super.persist(e);
		addHistoryRecord(new AccountHistoryContext(AccountHistoryOp.ACCOUNT_DELETED, e));
	}

	/**
	 * Adds an account history record
	 * @param context
	 * @throws EntityExistsException
	 */
	private void addHistoryRecord(final AccountHistoryContext context) {
		switch(context.getOp()) {
			// add account
			case ACCOUNT_ADDED: {
				final AccountHistory ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getAccount()));
				ah.setNotes(context.getAccount().typeDesc() + " created");
				ah.setStatus(AccountStatus.NEW);
				dao.persist(ah);
				break;
			}
				// delete account
			case ACCOUNT_DELETED: {
				final AccountHistory ah =
						entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(context.getAccount()));
				ah.setStatus(AccountStatus.DELETED);
				ah.setNotes(context.getAccount().typeDesc() + " marked as DELETED");
				dao.persist(ah);
				break;
			}
				// purge account
			case ACCOUNT_PURGED: {
				// add history record to parentAccount
				final Account parent = context.getAccount().getParent();
				if(parent != null) {
					final AccountHistory ah = entityAssembler.assembleEntity(AccountHistory.class, new EntityCache(parent));
					ah.setStatus(AccountStatus.DELETED);
					ah.setNotes("Child account: " + context.getAccount().typeDesc() + "'" + context.getAccount().descriptor()
							+ "' DELETED");
					dao.persist(ah);
				}
				break;
			}

		}// switch

	}

	@Override
	public IListingDataProvider<AccountHistory> getAccountHistoryDataProvider() {
		return new AccountHistoryDataProvider(dao);
	}

}
