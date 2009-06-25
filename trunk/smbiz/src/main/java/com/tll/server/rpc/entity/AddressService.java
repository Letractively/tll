/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;


/**
 * AddressService
 * @author jpk
 */
public class AddressService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public AddressService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "Address";
	}

}
