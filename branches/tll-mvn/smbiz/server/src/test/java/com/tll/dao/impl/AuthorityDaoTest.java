/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.Authority;

/**
 * AuthorityDaoTest
 * @author jpk
 */
@Test(groups = "dao", testName = "AuthorityDaoTest")
public class AuthorityDaoTest extends NamedEntityDaoTest<Authority> {

	/**
	 * Constructor
	 */
	public AuthorityDaoTest() {
		super(Authority.class, IAuthorityDao.class);
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
