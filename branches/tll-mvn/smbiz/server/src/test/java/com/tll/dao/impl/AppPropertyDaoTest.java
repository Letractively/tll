/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.AppProperty;

/**
 * AppPropertyDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "AppPropertyDaoTest")
public class AppPropertyDaoTest extends AbstractEntityDaoTest<AppProperty> {

	/**
	 * Constructor
	 */
	public AppPropertyDaoTest() {
		super(AppProperty.class, true);
	}

	@Override
	protected void assembleTestEntity(AppProperty e) throws Exception {
		// no-op
	}

	@Override
	protected void verifyLoadedEntityState(AppProperty e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getValue());
	}

}
