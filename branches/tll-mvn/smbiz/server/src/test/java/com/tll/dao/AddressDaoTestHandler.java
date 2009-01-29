/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Address;

/**
 * AddressDaoTestHandler
 * @author jpk
 */
public class AddressDaoTestHandler extends AbstractEntityDaoTestHandler<Address> {

	@Override
	public Class<Address> entityClass() {
		return Address.class;
	}

	@Override
	public void assembleTestEntity(Address e) throws Exception {
		// no-op
	}

	@Override
	public void verifyLoadedEntityState(Address e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getAddress1(), "Address address1 is null");
	}

	@Override
	public void alterTestEntity(Address e) {
		super.alterTestEntity(e);
		e.setAddress1("alter");
	}

	@Override
	public void verifyEntityAlteration(Address e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertEquals(e.getAddress1(), "alter", "The address alteration failed");
	}

}
