/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.bind;

/**
 * IConverter
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 * @author jpk
 * @param <O> The output type
 * @param <I> The input type
 */
public interface IConverter<O, I> {

	/**
	 * Convert an object from one type to another.
	 * @param original The object to convert
	 * @return The converted object
	 */
	O convert(I original);
}
