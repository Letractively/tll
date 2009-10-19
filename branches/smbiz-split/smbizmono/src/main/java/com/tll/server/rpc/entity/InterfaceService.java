/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.server.rpc.entity;


/**
 * InterfaceService
 * @author jpk
 */
public class InterfaceService extends AbstractPersistServiceImpl {

	/**
	 * Constructor
	 * @param context
	 */
	public InterfaceService(PersistContext context) {
		super(context);
	}

	@Override
	protected String getModelTypeName() {
		return "Interface";
	}

}
