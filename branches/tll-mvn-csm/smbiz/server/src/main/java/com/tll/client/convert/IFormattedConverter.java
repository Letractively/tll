/**
 * The Logic Lab
 * @author jpk
 * Jan 11, 2009
 */
package com.tll.client.convert;

import com.tll.client.util.GlobalFormat;

/**
 * IFormattedConverter
 * @param <O> The output type
 * @param <I> The input type
 * @author jpk
 */
public interface IFormattedConverter<O, I> extends IConverter<O, I> {

	/**
	 * @return The format.
	 */
	GlobalFormat getFormat();
}
