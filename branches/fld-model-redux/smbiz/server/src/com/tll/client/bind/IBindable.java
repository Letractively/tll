/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.bind;

import com.tll.client.model.PropertyPathException;

/**
 * IBindable
 * <p>
 * Indicates the ability to be "bound". The meaning of bound is context
 * dependent.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 * @author jpk
 */
public interface IBindable extends ISourcesPropertyChangeEvents {

	/**
	 * Retrieves the property value of the property identified by a property path.
	 * @param propPath The property path
	 * @return The non-<code>null</code> properties' held value
	 * @throws PropertyPathException When the given property is invalid.
	 */
	Object getProperty(String propPath) throws PropertyPathException;

	/**
	 * Sets a properties' value identified by a property path.
	 * @param propPath The property path
	 * @param value The property value to set
	 * @throws PropertyPathException When the property path is invalid or the
	 *         given value is not compatible with this property.
	 */
	void setProperty(String propPath, Object value) throws PropertyPathException;
}
