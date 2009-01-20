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
import com.tll.dao.impl.IOrderItemTransDao;
import com.tll.model.OrderItemTrans;

/**
 * OrderItemTransDao
 * @author jpk
 */
public class OrderItemTransDao extends EntityDao<OrderItemTrans> implements IOrderItemTransDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public OrderItemTransDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<OrderItemTrans> getEntityClass() {
		return OrderItemTrans.class;
	}

}
