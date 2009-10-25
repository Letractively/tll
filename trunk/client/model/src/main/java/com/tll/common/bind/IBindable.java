/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.common.bind;

import com.tll.common.model.PropertyPathException;

/**
 * IBindable
 * <p>
 * Indicates the ability to be "bound". The meaning of bound is context
 * dependent.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IBindable extends ISourcesPropertyChangeEvents {

	/**
	 * Retrieves the property value of the property identified by a property path.
	 * @param propPath The property path
	 * @return The properties' held value which may be <code>null</code>.
	 * @throws PropertyPathException When the given property is invalid.
	 */
	Object getProperty(String propPath) throws PropertyPathException;

	/**
	 * Sets a properties' value identified by a property path.
	 * @param propPath The property path
	 * @param value The property value to set
	 * @throws PropertyPathException When the property path is invalid or the
	 *         given value is not compatible with this property.
	 * @throws IllegalArgumentException When the given value leads to an error of
	 *         any kind
	 */
	void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException;
}
