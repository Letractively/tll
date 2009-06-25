/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity.mock;

import com.tll.server.rpc.entity.AbstractPersistServiceImpl;
import com.tll.server.rpc.entity.PersistContext;

/**
 * TestAddressService
 * @author jpk
 */
public class TestAddressService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public TestAddressService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "Address";
	}

}
