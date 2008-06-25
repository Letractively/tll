/**
 * 
 */
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
import com.tll.dao.hibernate.EntityDao;
import com.tll.dao.impl.IAppPropertyDao;
import com.tll.model.impl.AppProperty;
import com.tll.model.key.INameKey;

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

	public AppProperty load(INameKey<? extends AppProperty> nameKey) {
		try {
			return findEntity(CriteriaFactory.buildEntityCriteria(nameKey, true));
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

}
