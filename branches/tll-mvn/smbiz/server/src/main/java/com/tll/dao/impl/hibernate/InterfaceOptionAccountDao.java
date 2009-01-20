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
import com.tll.dao.impl.IInterfaceOptionAccountDao;
import com.tll.model.InterfaceOptionAccount;

/**
 * InterfaceOptionAccountDao
 * @author jpk
 */
public class InterfaceOptionAccountDao extends TimeStampEntityDao<InterfaceOptionAccount> implements IInterfaceOptionAccountDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public InterfaceOptionAccountDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<InterfaceOptionAccount> getEntityClass() {
		return InterfaceOptionAccount.class;
	}

}
