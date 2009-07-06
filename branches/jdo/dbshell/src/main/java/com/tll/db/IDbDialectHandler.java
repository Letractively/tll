/**
 * 
 */
package com.tll.db;

/**
 * Handles dialect specific db ops mainly translating dialect-specific db
 * exceptions attempting to add detail to the unchecked exception.
 * @author jpk
 */
public interface IDbDialectHandler {

	/**
	 * Determines if the given exception is due to an attempt to issue SQL
	 * commands to an unknown (prob. non-existant) database.
	 * @param re
	 * @return true/false
	 */
	boolean isUnknownDatabase(RuntimeException re);

	/**
	 * Determines if the given exception is due to an attempt to create an already
	 * created database.
	 * @param re
	 * @return true/false
	 */
	boolean isCreateAlreadyExist(RuntimeException re);

	/**
	 * Determines if the given exception is due to an attempt to drop an a
	 * non-existant database.
	 * @param re
	 * @return true/false
	 */
	boolean isDropNonExistant(RuntimeException re);

	/**
	 * Translates from dialect specific db exceptions to application specific
	 * exceptions.
	 * @param re The unchecked exception
	 * @return The translated exception
	 */
	RuntimeException translate(RuntimeException re);
}
