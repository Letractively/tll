/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Merchant;

/**
 * MerchantDaoTestHandler
 * @author jpk
 */
public class MerchantDaoTestHandler extends AbstractAccountDaoTestHandler<Merchant> {

	@Override
	public Class<Merchant> entityClass() {
		return Merchant.class;
	}

	@Override
	public void verifyLoadedEntityState(Merchant e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getStoreName(), "Merchant store name is null");
	}

}
