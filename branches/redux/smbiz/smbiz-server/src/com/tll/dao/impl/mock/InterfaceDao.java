/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IInterfaceDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.Interface;

public class InterfaceDao extends EntityDao<Interface> implements IInterfaceDao, IMockDao<Interface> {

	@Inject
	public InterfaceDao(Set<Interface> set) {
		super(Interface.class, set);
	}
}