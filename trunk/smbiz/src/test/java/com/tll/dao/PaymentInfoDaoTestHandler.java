/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.PaymentInfo;

/**
 * PaymentInfoDaoTestHandler
 * @author jpk
 */
public class PaymentInfoDaoTestHandler extends AbstractEntityDaoTestHandler<PaymentInfo> {

	@Override
	public Class<PaymentInfo> entityClass() {
		return PaymentInfo.class;
	}

	@Override
	public void assembleTestEntity(PaymentInfo e) throws Exception {
		// no-op
	}

	@Override
	public void verifyLoadedEntityState(PaymentInfo e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getPaymentData(), "No PaymentData loaded");
	}

	@Override
	public void alterTestEntity(PaymentInfo e) {
		super.alterTestEntity(e);
		e.getPaymentData().setBankName("altered");
	}

	@Override
	public void verifyEntityAlteration(PaymentInfo e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getPaymentData().getBankName(), "altered");
	}

}
