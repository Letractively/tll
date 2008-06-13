/**
 * 
 */
package com.tll.dao.dialect;

import java.sql.SQLException;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang.WordUtils;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

import com.tll.dao.IDbDialectHandler;
import com.tll.model.IEntity;

/**
 * MySql implementation of {@link IDbDialectHandler}
 * @author jpk
 */
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
		String msg = re.getMessage();
		return msg != null && (msg.indexOf(Integer.toString(ER_BAD_DB_ERROR)) >= 0);
	}

	public boolean isCreateAlreadyExist(RuntimeException re) {
		String msg = re.getMessage();
		return msg != null && (msg.indexOf(Integer.toString(ER_DB_CREATE_EXISTS)) >= 0);
	}

	public boolean isDropNonExistant(RuntimeException re) {
		String msg = re.getMessage();
		return msg != null && (msg.indexOf(Integer.toString(ER_DB_DROP_EXISTS)) >= 0);
	}

	public RuntimeException translate(RuntimeException re) {
		if(re instanceof PersistenceException) {
			return re;
		}
		if(re instanceof JDBCException) {
			JDBCException je = (JDBCException) re;
			SQLException sqle = je.getSQLException();
			if(sqle != null) {
				final int ec = sqle.getErrorCode();
				final String msg = sqle.getMessage();
				if(ec == ER_DUP_ENTRY || ec == ER_DUP_ENTRY_AUTOINCREMENT_CASE || ec == ER_DUP_ENTRY_WITH_KEY_NAME) {
					if(msg.indexOf(PK_PREFIX) != -1) {
						return new EntityExistsException(IEntity.PK_FIELDNAME);
					}
					if(msg.startsWith("Duplicate entry '")) {
						String key = WordUtils.capitalize(msg.substring(17, msg.lastIndexOf('\'')));
						return new EntityExistsException(key);
					}
					return new EntityExistsException("Business EntityKey"); // fallback
				}
			}
		}
		else if(re instanceof HibernateException) {
			// TODO any special handling for HibernateException s?
		}
		return re;
	}
}
