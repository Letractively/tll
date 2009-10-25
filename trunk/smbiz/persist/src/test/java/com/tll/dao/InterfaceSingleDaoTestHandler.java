/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.InterfaceSingle;

/**
 * InterfaceSingleDaoTestHandler
 * @author jpk
 */
public class InterfaceSingleDaoTestHandler extends AbstractInterfaceDaoTestHandler<InterfaceSingle> {

	@Override
	public Class<InterfaceSingle> entityClass() {
		return InterfaceSingle.class;
	}

}
