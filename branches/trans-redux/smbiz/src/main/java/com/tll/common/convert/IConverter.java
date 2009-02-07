/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.common.convert;

/**
 * IConverter - Indicates the ability to convert from one type to another type.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 * @param <O> The output type
 * @param <I> The input type
 */
public interface IConverter<O, I> {

	/**
	 * Converts the given input instance to an instance of the slated output type.
	 * @param o The object to render
	 * @return The rendered object
	 * @throws IllegalArgumentException When the given object can't be converted
	 *         for whatever reason.
	 */
	O convert(I o) throws IllegalArgumentException;
}
