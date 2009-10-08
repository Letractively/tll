/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.PaymentTrans;

/**
 * PaymentTransDaoTestHandler
 * @author jpk
 */
public class PaymentTransDaoTestHandler extends AbstractEntityDaoTestHandler<PaymentTrans> {

	@Override
	public Class<PaymentTrans> entityClass() {
		return PaymentTrans.class;
	}

	@Override
	public void assembleTestEntity(PaymentTrans e) throws Exception {
		// no-op
	}

	@Override
	public void verifyLoadedEntityState(PaymentTrans e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAuthNum(), "No Auth Num loaded");
	}

	@Override
	public void alterTestEntity(PaymentTrans e) {
		super.alterTestEntity(e);
		e.setAuthNum("altered");
	}

	@Override
	public void verifyEntityAlteration(PaymentTrans e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getAuthNum(), "altered");
	}

}
