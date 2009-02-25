/**
 * The Logic Lab
 * @author jpk Nov 21, 2007
 */
package com.tll.server;

/**
 * SecurityMode
 * @author jpk
 */
public enum SecurityMode {
	/**
	 * Use the Acegi security framework.
	 */
	ACEGI,
	/**
	 * Use NO security framework.
	 */
	NONE;
}