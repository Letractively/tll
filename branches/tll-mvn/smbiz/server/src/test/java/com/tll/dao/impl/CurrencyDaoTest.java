/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.Currency;

/**
 * CurrencyDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "CurrencyDaoTest")
public class CurrencyDaoTest extends NamedEntityDaoTest<Currency> {

	/**
	 * Constructor
	 */
	public CurrencyDaoTest() {
		super(Currency.class, ICurrencyDao.class);
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
