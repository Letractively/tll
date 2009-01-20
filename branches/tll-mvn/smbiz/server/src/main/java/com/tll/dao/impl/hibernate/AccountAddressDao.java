/**
 * 
 */
package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IAccountAddressDao;
import com.tll.model.AccountAddress;
import com.tll.model.key.NameKey;

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

	@Override
	public AccountAddress load(NameKey<? extends AccountAddress> nameKey) {
		return (AccountAddress) loadByName(nameKey);
	}
}
