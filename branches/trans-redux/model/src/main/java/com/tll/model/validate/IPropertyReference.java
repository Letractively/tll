/**
 * The Logic Lab
 * @author jpk Dec 22, 2007
 */
package com.tll.model.validate;

/**
 * IPropertyReference - Hook to extract a property name. Used by bean level
 * validators to identify the target property.
 * @author jpk
 */
interface IPropertyReference {

	/**
	 * @return The target property name.
	 */
	String getPropertyReference();
}
