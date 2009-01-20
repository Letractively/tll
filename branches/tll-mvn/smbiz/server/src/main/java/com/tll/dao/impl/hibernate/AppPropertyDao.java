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
import com.tll.dao.impl.IAppPropertyDao;
import com.tll.model.AppProperty;
import com.tll.model.key.NameKey;

/**
 * AppPropertyDao
 * @author jpk
 */
public class AppPropertyDao extends EntityDao<AppProperty> implements IAppPropertyDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public AppPropertyDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<AppProperty> getEntityClass() {
		return AppProperty.class;
	}

	@Override
	public AppProperty load(NameKey<? extends AppProperty> nameKey) {
		return (AppProperty) loadByName(nameKey);
	}
}
