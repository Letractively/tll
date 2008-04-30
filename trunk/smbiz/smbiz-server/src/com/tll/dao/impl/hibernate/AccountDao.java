package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.IComparatorTranslator;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IAccountDao;
import com.tll.model.impl.Account;
import com.tll.model.key.INameKey;

/**
 * AccountDao
 * @author jpk
 */
public class AccountDao extends TimeStampEntityDao<Account> implements IAccountDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public AccountDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<Account> getEntityClass() {
		return Account.class;
	}

	public Account load(INameKey<? extends Account> nameKey) {
		try {
			return findEntity(CriteriaFactory.buildEntityCriteria(nameKey, true));
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

}
