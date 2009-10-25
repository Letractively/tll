/**
 * The Logic Lab
 * @author jpk
 * @since Jul 2, 2009
 */
package com.tll.dao;

/**
 * IDbShell - Definition for handling stubbing ops for a db based on a defined
 * schema.
 * @author jpk
 */
public interface IDbShell {

	/**
	 * Creates the database. If the db already exists, nothing happens.
	 * @return <code>true</code> if the db was actually created as a result of
	 *         calling this method and <code>false<code> if the db already exists.
	 */
	boolean create();

	/**
	 * Deletes the database. If the db doesn't exist, nothing happens.
	 * @return <code>true</code> if the db was actually deleted as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is found not to exist.
	 */
	boolean delete();

	/**
	 * Clears the database of all data. If the db doesn't exist, nothing happens.
	 * @return <code>true</code> if the db was actually cleared as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is <code>not</code> cleared by way of
	 *         this method.
	 */
	boolean clear();

	/**
	 * Clears the database targeted by the given db session ref of all data. If
	 * the db doesn't exist, nothing happens.
	 * @param dbSession The session that targets the desired db to clear
	 * @return <code>true</code> if the db was actually cleared as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is <code>not</code> cleared by way of
	 *         this method.
	 */
	boolean clear(Object dbSession);

	/**
	 * Adds data to the db where serving to stub the db. The db <em>must</em>
	 * already exist else an error is raised.
	 * @return <code>true</code> if the db was actually stubbed with the stub data
	 *         as a result of calling this method.
	 */
	boolean stub();

	/**
	 * Adds data to the db targeted by the given db sesssion ref serving to stub
	 * the db. The db <em>must</em> already exist else an error is raised.
	 * @param dbSession The session that targets the desired db to stub
	 * @return <code>true</code> if the db was actually stubbed with the stub data
	 *         as a result of calling this method.
	 */
	boolean stub(Object dbSession);

	/**
	 * Stubs or re-stubs the data in the db creating the db if not already created
	 * and/or clearing the the db if it contains existing data.
	 */
	void restub();

	/**
	 * Stubs or re-stubs the data in the targeted db creating the db if not already created
	 * and/or clearing the the db if it contains existing data.
	 * @param dbSession The session that targets the desired db
	 */
	void restub(Object dbSession);
}