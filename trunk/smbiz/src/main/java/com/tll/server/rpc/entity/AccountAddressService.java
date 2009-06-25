/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2007
 */
package com.tll.server.rpc.entity;


/**
 * AccountAddressService
 * @author jpk
 */
public final class AccountAddressService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public AccountAddressService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "Account Address";
	}

}
