/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.InterfaceMulti;

/**
 * InterfaceSingleDaoTestHandler
 * @author jpk
 */
public class MultiInterfaceDaoTestHandler extends AbstractInterfaceDaoTestHandler<InterfaceMulti> {

	@Override
	public Class<InterfaceMulti> entityClass() {
		return InterfaceMulti.class;
	}

}
