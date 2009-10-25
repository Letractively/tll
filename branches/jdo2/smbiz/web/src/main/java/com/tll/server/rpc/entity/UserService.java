/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;


/**
 * UserService
 * @author jpk
 */
public class UserService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public UserService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "User";
	}

}
