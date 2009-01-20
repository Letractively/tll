/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.PaymentTrans;

/**
 * PaymentTransDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "PaymentTransDaoTest")
public class PaymentTransDaoTest extends AbstractDaoTest<PaymentTrans> {

	/**
	 * Constructor
	 */
	public PaymentTransDaoTest() {
		super(PaymentTrans.class, IPaymentTransDao.class);
	}

	@Override
	protected void assembleTestEntity(PaymentTrans e) throws Exception {
		// no-op
	}

	@Override
	protected void verifyLoadedEntityState(PaymentTrans e) throws Exception {
		Assert.assertNotNull(e.getAuthNum(), "No Auth Num loaded");
	}

	@Override
	protected void alterEntity(PaymentTrans e) {
		e.setAuthNum("altered");
	}

	@Override
	protected void verifyEntityAlteration(PaymentTrans e) throws Exception {
		Assert.assertEquals(e.getAuthNum(), "altered");
	}

}
