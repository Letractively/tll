/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Currency;

/**
 * CurrencyDaoTestHandler
 * @author jpk
 */
public class CurrencyDaoTestHandler extends AbstractEntityDaoTestHandler<Currency> {

	@Override
	public Class<Currency> entityClass() {
		return null;
	}

	@Override
	public void assembleTestEntity(Currency e) throws Exception {
		// no-op
	}

	@Override
	public void verifyLoadedEntityState(Currency e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getIso4217());
	}

}
