package com.tll.service.entity.impl.pymnt;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IPaymentTransDao;
import com.tll.model.EntityAssembler;
import com.tll.model.PaymentTrans;
import com.tll.service.entity.EntityService;

/**
 * PaymentTransService - {@link IPaymentTransService} impl
 * @author jpk
 */
@Transactional
public class PaymentTransService extends EntityService<PaymentTrans, IPaymentTransDao> implements IPaymentTransService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public PaymentTransService(IPaymentTransDao dao, EntityAssembler entityAssembler) {
		super(IPaymentTransDao.class, dao, entityAssembler);
	}

	@Override
	public Class<PaymentTrans> getEntityClass() {
		return PaymentTrans.class;
	}
}
