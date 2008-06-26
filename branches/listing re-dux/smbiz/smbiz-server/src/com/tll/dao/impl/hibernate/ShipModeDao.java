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
import com.tll.dao.impl.IShipModeDao;
import com.tll.model.impl.ShipMode;
import com.tll.model.key.NameKey;

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

	@Override
	public ShipMode load(NameKey<ShipMode> nameKey) {
		return (ShipMode) loadByName(nameKey);
	}

}
