/**
 * 
 */
package com.tll.dao.jdo;



/**
 * MySql implementation of {@link IDbDialectHandler}
 * @author jpk
 */
// TODO eliminate hibernate dependencies!
public class MySqlDialectHandler implements IDbDialectHandler {

	/**
	 * The string prefix that is presumed to exist for all primary key names in
	 * MySql.
	 */
	public static final String PK_PREFIX = "PK"; // TODO verify syntax

	// [pertinent] mysql error codes
	/*
	 * #Error: 1062 SQLSTATE: 23000 (ER_DUP_ENTRY) Message: Duplicate entry '%s'
	 * for key %d
	 */

	public static final int ER_DB_CREATE_EXISTS = 1007;

	public static final int ER_DB_DROP_EXISTS = 1008;

	/**
	 * Message: Unknown database '%s'
	 */
	public static final int ER_BAD_DB_ERROR = 1049;

	public static final int ER_DUP_ENTRY = 1062;

	public static final int ER_DUP_ENTRY_AUTOINCREMENT_CASE = 1559;

	public static final int ER_DUP_ENTRY_WITH_KEY_NAME = 1582;

	public boolean isUnknownDatabase(RuntimeException re) {
		final String msg = re.getMessage();
		return msg != null && (msg.indexOf(Integer.toString(ER_BAD_DB_ERROR)) >= 0);
	}

	public boolean isCreateAlreadyExist(RuntimeException re) {
		final String msg = re.getMessage();
		return msg != null && (msg.indexOf(Integer.toString(ER_DB_CREATE_EXISTS)) >= 0);
	}

	public boolean isDropNonExistant(RuntimeException re) {
		final String msg = re.getMessage();
		return msg != null && (msg.indexOf(Integer.toString(ER_DB_DROP_EXISTS)) >= 0);
	}
}
