/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IAddressDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.Address;

public class AddressDao extends EntityDao<Address> implements IAddressDao, IMockDao<Address> {

	@Inject
	public AddressDao(Set<Address> set) {
		super(Address.class, set);
	}
}