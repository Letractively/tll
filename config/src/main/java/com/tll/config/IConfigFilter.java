/**
 * The Logic Lab
 * @author jpk Apr 5, 2009
 */
package com.tll.config;

/**
 * IConfigFilter - A filter for config keys enabling query-ability of a
 * particular {@link Config}.
 * @author jpk
 */
public interface IConfigFilter {

	/**
	 * Accept the given config key in this filter.
	 * @param keyName the key name of the targeted {@link Config} instance.
	 * @return <code>true</code> or <code>false</code>
	 */
	boolean accept(String keyName);
}
