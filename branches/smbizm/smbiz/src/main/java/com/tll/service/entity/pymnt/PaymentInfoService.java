package com.tll.service.entity.pymnt;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.PaymentInfo;
import com.tll.service.entity.NamedEntityService;

/**
 * PaymentInfoService - {@link IPaymentInfoService} impl
 * @author jpk
 */
@Transactional
public class PaymentInfoService extends NamedEntityService<PaymentInfo> implements IPaymentInfoService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public PaymentInfoService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<PaymentInfo> getEntityClass() {
		return PaymentInfo.class;
	}
}
