/**
 * The Logic Lab
 * @author jpk
 * @since Jan 16, 2010
 */
package com.tll.test;

/**
 * ITestEnvironment - Generic interface for setting up and tearing down test
 * environments.
 * @author jpk
 */
public interface ITestEnvironment {

	/**
	 * Sets up the test environment.
	 */
	void setupTestEnvironment();

	/**
	 * Tears down the test environment.
	 */
	void teardownTestEnvironment();
}
