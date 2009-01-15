/**
 * The Logic Lab
 * @author jpk
 * Apr 17, 2008
 */
package com.tll.client.model;

/**
 * ISelfFormattingPropertyValue - A property value able to represent its held
 * value as a String without a format directive.
 * @author jpk
 */
public interface ISelfFormattingPropertyValue extends IPropertyValue {

	/**
	 * @return The value as a String. If the value is <code>null</code>,
	 *         <code>null</code> is returned.
	 */
	String asString();
}
