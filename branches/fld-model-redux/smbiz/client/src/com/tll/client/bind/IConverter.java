/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.bind;

/**
 * IConverter
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>* @author jpk
 */
public interface IConverter {

	/**
	 * Convert an object from one type to another.
	 * @param original The object to convert
	 * @return The converted object
	 */
	Object convert(Object original);
}
