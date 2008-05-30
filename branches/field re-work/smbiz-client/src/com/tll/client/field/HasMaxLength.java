/**
 * The Logic Lab
 * @author jpk Jan 22, 2008
 */
package com.tll.client.field;

/**
 * HasMaxLength - Incidates support for a max length property. Used in field
 * rendering and validation.
 * @author jpk
 */
public interface HasMaxLength {

	/**
	 * @return The maximum allowed length. If <code>-1</code>, the max length
	 *         is <em>not</em> specified.
	 */
	int getMaxLen();

	/**
	 * Sets the maximum allowable length.
	 * @param maxLen The max allowed length or <code>-1</code> to declare it
	 *        unspecified.
	 */
	void setMaxLen(int maxLen);
}
