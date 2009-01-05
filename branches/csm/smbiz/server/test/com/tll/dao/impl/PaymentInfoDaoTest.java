/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.impl.PaymentInfo;

/**
 * PaymentInfoDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "PaymentInfoDaoTest")
public class PaymentInfoDaoTest extends NamedEntityDaoTest<PaymentInfo> {

	/**
	 * Constructor
	 */
	public PaymentInfoDaoTest() {
		super(PaymentInfo.class, IPaymentInfoDao.class);
	}

	@Override
	protected void assembleTestEntity(PaymentInfo e) throws Exception {
		// no-op
	}

	@Override
	protected void verifyLoadedEntityState(PaymentInfo e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getPaymentData(), "No PaymentData loaded");
	}

	@Override
	protected void alterEntity(PaymentInfo e) {
		super.alterEntity(e);
		e.getPaymentData().setBankName("altered");
	}

	@Override
	protected void verifyEntityAlteration(PaymentInfo e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getPaymentData().getBankName(), "altered");
	}

}
