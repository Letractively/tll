/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IInterfaceOptionAccountDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.InterfaceOptionAccount;

public class InterfaceOptionAccountDao extends EntityDao<InterfaceOptionAccount> implements IInterfaceOptionAccountDao,
		IMockDao<InterfaceOptionAccount> {

	@Inject
	public InterfaceOptionAccountDao(Set<InterfaceOptionAccount> set) {
		super(InterfaceOptionAccount.class, set);
	}
}