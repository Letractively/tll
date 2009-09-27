/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.Asp;

/**
 * AspAccountDaoTestHandler
 * @author jpk
 */
public class AspAccountDaoTestHandler extends AbstractAccountDaoTestHandler<Asp> {

	@Override
	public Class<Asp> entityClass() {
		return Asp.class;
	}

}
