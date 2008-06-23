/**
 * 
 */
package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.Criteria;
import com.tll.criteria.IComparatorTranslator;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IAccountAddressDao;
import com.tll.model.impl.AccountAddress;
import com.tll.model.key.INameKey;

/**
 * AccountAddressDao
 * @author jpk
 */
public class AccountAddressDao extends TimeStampEntityDao<AccountAddress> implements IAccountAddressDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public AccountAddressDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<AccountAddress> getEntityClass() {
		return AccountAddress.class;
	}

	public AccountAddress load(INameKey<? extends AccountAddress> nameKey) {
		try {
			final Criteria<AccountAddress> nc = new Criteria<AccountAddress>(AccountAddress.class);
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			return findEntity(nc);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

}
