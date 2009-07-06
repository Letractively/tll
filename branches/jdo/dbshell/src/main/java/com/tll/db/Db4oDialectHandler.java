/**
 * The Logic Lab
 * @author jpk
 * @since Jul 3, 2009
 */
package com.tll.db;


/**
 * Db4oDialectHandler
 * @author jpk
 */
public class Db4oDialectHandler implements IDbDialectHandler {

	@Override
	public boolean isCreateAlreadyExist(RuntimeException re) {
		return false;
	}

	@Override
	public boolean isDropNonExistant(RuntimeException re) {
		return false;
	}

	@Override
	public boolean isUnknownDatabase(RuntimeException re) {
		return false;
	}

	@Override
	public RuntimeException translate(RuntimeException re) {
		return null;
	}

}
