/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Address;

/**
 * AddressDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "AddressDaoTest")
public class AddressDaoTest extends AbstractDaoTest<Address> {

	/**
	 * Constructor
	 */
	public AddressDaoTest() {
		super(Address.class, IAddressDao.class);
	}

	@Override
	protected void assembleTestEntity(Address e) throws Exception {
		// no-op
	}

	@Override
	protected void verifyLoadedEntityState(Address e) throws Exception {
		Assert.assertNotNull(e.getAddress1(), "Address address1 is null");
	}

	@Override
	protected void alterEntity(Address e) {
		e.setAddress1("alter");
	}

	@Override
	protected void verifyEntityAlteration(Address e) throws Exception {
		Assert.assertEquals(e.getAddress1(), "alter", "The address alteration failed");
	}

}