package com.tll.dao;

/**
 * DaoMode
 */
public enum DaoMode {
	/**
	 * MOCK dao mode.
	 */
	MOCK(false, false),
	/**
	 * Hibernate ORM dao impl.
	 */
	HIBERNATE(true, true);

	private final boolean orm;
	private final boolean jpa;

	/**
	 * Constructor
	 * @param orm
	 * @param jpa
	 */
	private DaoMode(boolean orm, boolean jpa) {
		this.orm = orm;
		this.jpa = jpa;
	}

	/**
	 * @return <code>true</code> if this mode is Object relational mapping
	 */
	public boolean isOrm() {
		return orm;
	}

	/**
	 * @return <code>true</code> if this mode implements the Java Persistence
	 *         Architecture (JPA) api
	 */
	public boolean isJpa() {
		return jpa;
	}

}