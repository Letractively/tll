package com.tll.dao;

/**
 * IDbTrans - Contract for handling db transactions on a manual basis.
 * @author jpk
 */
public interface IDbTrans {

	/**
	 * Starts a new transaction.
	 * @throws IllegalStateException When a trans is already underway
	 */
	void startTrans() throws IllegalStateException;

	/**
	 * Marks the currently underway transaction to be committed when it ends.
	 * @throws IllegalStateException When no trans is currently underway
	 */
	void setComplete() throws IllegalStateException;

	/**
	 * Ends a currently in process transaction committing when
	 * {@link #setComplete()} was called and rolling back otherwise.
	 * @throws IllegalStateException When no db trans is currently underway.
	 */
	void endTrans() throws IllegalStateException;

	/**
	 * @return <code>true</code> if a trans is currently in progress.
	 */
	boolean isTransStarted();
}
