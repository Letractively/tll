package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IAccountDao;
import com.tll.model.impl.Account;
import com.tll.model.key.NameKey;

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

	@Override
	public Account load(NameKey<Account> nameKey) {
		return (Account) loadByName(nameKey);
	}
}
