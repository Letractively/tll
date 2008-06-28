/**
 * The Logic Lab
 * @author jpk Jan 22, 2008
 */
package com.tll.client.field;

import com.tll.client.util.GlobalFormat;

/**
 * HasFormat - Indicates formatting support.
 * @author jpk
 */
public interface HasFormat {

	/**
	 * @return The format.
	 */
	GlobalFormat getFormat();

	/**
	 * Sets the format.
	 * @param format The format to set
	 */
	void setFormat(GlobalFormat format);
}
