/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IPaymentInfoDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.key.IBusinessKeyFactory;

public class PaymentInfoDao extends EntityDao<PaymentInfo> implements IPaymentInfoDao, IMockDao<PaymentInfo> {

	@Inject
	public PaymentInfoDao(Set<PaymentInfo> set, IBusinessKeyFactory bkf) {
		super(PaymentInfo.class, set, bkf);
	}
}