/**
 * The Logic Lab
 * @author jpk
 * Nov 21, 2007
 */
package com.tll.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * JpaMode (Java Persistence Architecture)
 * @author jpk
 */
public enum JpaMode {
	/**
	 * Use Spring JPA management including Spring Transaction management
	 */
	SPRING,
	/**
	 * Use a local {@link EntityManager}
	 */
	LOCAL,
	/**
	 * Empty stub of {@link EntityManagerFactory} and {@link EntityManager}
	 * interfaces
	 */
	MOCK,
	/**
	 * Do not employ JPA
	 */
	NONE
}