package com.tll.dao;

/**
 * DaoMode
 */
public enum DaoMode {
	/**
	 * MOCK (in memory Object graph).
	 */
	MOCK,
	/**
	 * ORM (Object Relational Mapping)
	 */
	ORM;

	/**
	 * Does this dao mode indicate the employment of an actual disk-based
	 * datastore?
	 * @return true/false
	 */
	public boolean isDatastore() {
		return this == DaoMode.ORM;
	}
}