/**
 * The Logic Lab
 * @author jpk
 * Mar 31, 2009
 */
package com.tll;

import com.tll.dao.jdbc.DbShell;

/**
 * AbstractDbAwareTest - Provides extended classes with a {@link DbTestSupport}
 * ref.
 * @author jpk
 */
public abstract class AbstractDbAwareTest extends AbstractInjectedTest {

	private DbTestSupport dbSupport;

	/**
	 * @return The db support helper object that provides a {@link DbShell} and
	 *         transaction handling.
	 */
	protected final DbTestSupport getDbSupport() {
		if(dbSupport == null) {
			dbSupport = new DbTestSupport();
		}
		return dbSupport;
	}
}