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
import com.tll.dao.hibernate.EntityDao;
import com.tll.dao.impl.ICurrencyDao;
import com.tll.model.Currency;
import com.tll.model.key.NameKey;

/**
 * CurrencyDao
 * @author jpk
 */
public class CurrencyDao extends EntityDao<Currency> implements ICurrencyDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public CurrencyDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<Currency> getEntityClass() {
		return Currency.class;
	}

	@Override
	public Currency load(NameKey<? extends Currency> nameKey) {
		return (Currency) loadByName(nameKey);
	}
}
