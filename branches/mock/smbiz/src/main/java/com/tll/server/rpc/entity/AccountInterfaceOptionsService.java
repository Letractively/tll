/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;


/**
 * InterfaceOptionAccountService
 * @author jpk
 */
public class AccountInterfaceOptionsService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public AccountInterfaceOptionsService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "Account Interface Option";
	}

}
