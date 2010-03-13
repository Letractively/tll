package com.tll.dao;

/**
 * Determines whether or not a given exception is due to a db-level error of a
 * prescribed type.
 * @author jpk
 */
public interface IDbErrorDeterminer {

	/**
	 * Attempt to create the db but the db already exists?
	 * @param e
	 * @return true/false
	 */
	boolean isCreateAlreadyExist(Exception e);

	/**
	 * Attempt to drop the db but the db does not exist?
	 * @param e
	 * @return true/false
	 */
	boolean isDropNonExistant(Exception e);

	/**
	 * Attempt to act upon a given table but the table does not exist?
	 * @param e
	 * @return true/false
	 */
	boolean isTableNonExistant(Exception e);

	/**
	 * Attempt to create a given db table but the table already exists? 
	 * @param e
	 * @return true/false
	 */
	boolean isTableAlreadyExists(Exception e);

}