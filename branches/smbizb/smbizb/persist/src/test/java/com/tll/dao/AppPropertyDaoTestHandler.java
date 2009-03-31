/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.AppProperty;

/**
 * AppPropertyDaoTestHandler
 * @author jpk
 */
public class AppPropertyDaoTestHandler extends AbstractEntityDaoTestHandler<AppProperty> {

	@Override
	public Class<AppProperty> entityClass() {
		return AppProperty.class;
	}

	@Override
	public void assembleTestEntity(AppProperty e) throws Exception {
		// no-op
	}

	@Override
	public void verifyLoadedEntityState(AppProperty e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getValue());
	}

}
