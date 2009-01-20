package com.tll.service.entity.impl.pymnt;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IPaymentInfoDao;
import com.tll.model.EntityAssembler;
import com.tll.model.PaymentInfo;
import com.tll.service.entity.EntityService;

/**
 * PaymentInfoService - {@link IPaymentInfoService} impl
 * @author jpk
 */
@Transactional
public class PaymentInfoService extends EntityService<PaymentInfo, IPaymentInfoDao> implements IPaymentInfoService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public PaymentInfoService(IPaymentInfoDao dao, EntityAssembler entityAssembler) {
		super(IPaymentInfoDao.class, dao, entityAssembler);
	}

	@Override
	public Class<PaymentInfo> getEntityClass() {
		return PaymentInfo.class;
	}
}
