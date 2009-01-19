/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IPaymentTransDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.PaymentTrans;
import com.tll.model.key.IBusinessKeyFactory;

/**
 * PaymentTransDao
 * @author jpk
 */
public class PaymentTransDao extends EntityDao<PaymentTrans> implements IPaymentTransDao, IMockDao<PaymentTrans> {

	@Inject
	public PaymentTransDao(Set<PaymentTrans> set, IBusinessKeyFactory bkf) {
		super(PaymentTrans.class, set, bkf);
	}

}
