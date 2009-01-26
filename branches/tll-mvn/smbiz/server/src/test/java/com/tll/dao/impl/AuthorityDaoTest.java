/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Authority;

/**
 * AuthorityDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "AuthorityDaoTest")
public class AuthorityDaoTest extends AbstractEntityDaoTest<Authority> {

	/**
	 * Constructor
	 */
	public AuthorityDaoTest() {
		super(Authority.class, true);
	}

	@Override
	protected void assembleTestEntity(Authority e) throws Exception {
		// no-op
	}

	@Override
	protected void beforeMethodHook() {
		super.beforeMethodHook();
		disableDataStoreCaching();
	}

}
