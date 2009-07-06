/**
 * 
 */
package com.tll.db;


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

	/**
	 * Interrogates the given {@link JDBCException} returning either the
	 * appropriate duplicate error message or <code>null</code> if the given
	 * {@link JDBCException} does not stem from a dupliate entry violation.
	 * @param je The {@link JDBCException} to interrogate
	 * @return The appropriate duplicate error message or <code>null</code>
	 */
	/*
	private String getDuplicateEntryMessage(JDBCException je) {
		final SQLException sqle = (je == null ? null : je.getSQLException());
		if(sqle != null) {
			final int ec = sqle.getErrorCode();
			if(ec == ER_DUP_ENTRY || ec == ER_DUP_ENTRY_AUTOINCREMENT_CASE || ec == ER_DUP_ENTRY_WITH_KEY_NAME) {
				final String msg = sqle.getMessage();
				if(msg.indexOf(PK_PREFIX) != -1) {
					return "Duplicate primary key";
				}
				if(msg.startsWith("Duplicate entry '")) {
					return WordUtils.capitalize(msg.substring(17, msg.lastIndexOf('\'')));
				}
				return "Duplicate database record"; // fallback
			}
		}
		return null;
	}
	 */

	/**
	 * Extracts a JDBCException from a Throwble.
	 * @return The found JDBCException or <code>null</code> if not found
	 */
	/*
	private JDBCException extractJdbcException(Throwable t) {
		if(t instanceof JDBCException) return (JDBCException) t;
		do {
			if((t = ExceptionUtils.getCause(t)) instanceof JDBCException) {
				return (JDBCException) t;
			}
		} while(t != null);
		return null;
	}
	 */

	public RuntimeException translate(RuntimeException re) {
		/*
		if(re instanceof PersistenceException) {
			// HACK: check for ConstraintViolationException since hibernate 3.3.1
			// doesn't
			// seem to be translating ConstraintViolationExceptions to
			// EntityExistsViolations in
			// AbstractEntityManager.throwPersistenceException(HibernateException)
			// anymore!!
			String dem = getDuplicateEntryMessage(extractJdbcException(re));
			if(dem != null) {
				return new EntityExistsException(dem);
			}
			// END HACK
			return re;
		}
		if(re instanceof JDBCException) {
			JDBCException je = (JDBCException) re;
			String dem = getDuplicateEntryMessage(je);
			if(dem != null) {
				return new EntityExistsException(dem);
			}
		}
		else if(re instanceof HibernateException) {
			// TODO any special handling for HibernateException s?
		}
		 */
		// no translation made so return original exception
		return re;
	}
}
