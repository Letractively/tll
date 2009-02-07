/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.Authority;

/**
 * AuthorityDaoTestHandler
 * @author jpk
 */
public class AuthorityDaoTestHandler extends AbstractEntityDaoTestHandler<Authority> {

	@Override
	public Class<Authority> entityClass() {
		return Authority.class;
	}

	@Override
	public void assembleTestEntity(Authority e) throws Exception {
		// no-op
	}
}
