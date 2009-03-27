/**
 * The Logic Lab
 * @author jpk
 * May 21, 2008
 */
package com.tll.config;

/**
 * IConfigKeyProvider - Serves as a way to extract a filtered subset of
 * configuration properties from a {@link Config} instance.
 * @author jpk
 */
public interface IConfigKeyProvider {

	/**
	 * @return The provided key names.
	 */
	String[] getConfigKeys();
}
