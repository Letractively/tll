/**
 * The Logic Lab
 * @author jpk
 * Feb 26, 2009
 */
package com.tll.client.ui.field;

import java.util.Map;

/**
 * AbstractDataField - A field that limits its value to a collection of
 * values.
 * @param <T> the value type
 * @author jpk
 */
public abstract class AbstractDataField<T> extends AbstractField<T> {

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 */
	public AbstractDataField(String name, String propName, String labelText, String helpText) {
		super(name, propName, labelText, helpText);
	}

	/**
	 * Set or reset the field data. <br>
	 * @param data Map of value/name pairs keyed by <em>value</em> where each key
	 *        holds the token for use in the ui.
	 */
	public abstract void setData(Map<String, String> data);
	
	/**
	 * Adds a single value/name data item.
	 * @param name the presentation name
	 * @param value the field value
	 */
	public abstract void addDataItem(String name, String value);

	/**
	 * Removes a single data item
	 * @param value the field value data item value to remove
	 */
	public abstract void removeDataItem(String value);
}
