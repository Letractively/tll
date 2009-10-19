package com.tll.service.entity.pymnt;

import javax.validation.ValidatorFactory;

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
	 * @param vfactory
	 */
	@Inject
	public PaymentTransService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<PaymentTrans> getEntityClass() {
		return PaymentTrans.class;
	}
}
