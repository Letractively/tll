package com.tll.service.entity.pymnt;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.PaymentTrans;
import com.tll.service.entity.EntityService;

/**
 * PaymentTransService - {@link IPaymentTransService} impl
 * @author jpk
 */
@Transactional
public class PaymentTransService extends EntityService<PaymentTrans> implements IPaymentTransService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public PaymentTransService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<PaymentTrans> getEntityClass() {
		return PaymentTrans.class;
	}
}