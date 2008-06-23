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
import com.tll.dao.impl.IShipModeDao;
import com.tll.model.impl.ShipMode;
import com.tll.model.key.INameKey;

/**
 * ShipModeDao
 * @author jpk
 */
public class ShipModeDao extends TimeStampEntityDao<ShipMode> implements IShipModeDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public ShipModeDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<ShipMode> getEntityClass() {
		return ShipMode.class;
	}

	public ShipMode load(INameKey<? extends ShipMode> nameKey) {
		try {
			final Criteria<ShipMode> nc = new Criteria<ShipMode>(ShipMode.class);
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			return findEntity(nc);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

}
