/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Currency;

/**
 * CurrencyDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "CurrencyDaoTest")
public class CurrencyDaoTest extends AbstractEntityDaoTest<Currency> {

	/**
	 * Constructor
	 */
	public CurrencyDaoTest() {
		super(Currency.class, true);
	}

	@Override
	protected void assembleTestEntity(Currency e) throws Exception {
		// no-op
	}

	@Override
	protected void verifyLoadedEntityState(Currency e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getIso4217());
	}

}
