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
import com.tll.dao.impl.IAuthorityDao;
import com.tll.model.impl.Authority;
import com.tll.model.key.NameKey;

/**
 * AuthorityDao
 * @author jpk
 */
public class AuthorityDao extends EntityDao<Authority> implements IAuthorityDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public AuthorityDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<Authority> getEntityClass() {
		return Authority.class;
	}

	@Override
	public Authority load(NameKey<Authority> nameKey) {
		return (Authority) loadByName(nameKey);
	}
}
