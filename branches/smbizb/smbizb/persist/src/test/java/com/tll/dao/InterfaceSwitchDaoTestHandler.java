/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.InterfaceSwitch;

/**
 * InterfaceSingleDaoTestHandler
 * @author jpk
 */
public class InterfaceSwitchDaoTestHandler extends AbstractInterfaceDaoTestHandler<InterfaceSwitch> {

	@Override
	public Class<InterfaceSwitch> entityClass() {
		return InterfaceSwitch.class;
	}

}
