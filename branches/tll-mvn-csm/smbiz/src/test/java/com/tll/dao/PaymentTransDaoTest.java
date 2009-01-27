/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.PaymentTrans;

/**
 * PaymentTransDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "PaymentTransDaoTest")
public class PaymentTransDaoTest extends AbstractEntityDaoTest<PaymentTrans> {

	/**
	 * Constructor
	 */
	public PaymentTransDaoTest() {
		super(PaymentTrans.class, true);
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
