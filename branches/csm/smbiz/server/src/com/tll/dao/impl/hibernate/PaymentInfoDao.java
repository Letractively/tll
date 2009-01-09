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
import com.tll.dao.impl.IPaymentInfoDao;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.key.NameKey;

/**
 * PaymentInfoDao
 * @author jpk
 */
public class PaymentInfoDao extends EntityDao<PaymentInfo> implements IPaymentInfoDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public PaymentInfoDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<PaymentInfo> getEntityClass() {
		return PaymentInfo.class;
	}

	@Override
	public PaymentInfo load(NameKey<? extends PaymentInfo> nameKey) {
		return (PaymentInfo) loadByName(nameKey);
	}
}