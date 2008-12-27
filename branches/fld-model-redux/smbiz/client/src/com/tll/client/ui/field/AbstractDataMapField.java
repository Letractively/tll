/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

/**
 * AbstractDataMapField - Supports the ability to map a value to a mapped value.
 * Form controls involving multiple selection choices such as list boxes rely on
 * data maps.
 * @author jpk
 */
public abstract class AbstractDataMapField extends AbstractField<String> {

	/**
	 * Map of name/value pairs.<br>
	 * FORMAT: key: presentation name, val: actual value<br>
	 */
	protected Map<String, String> dataMap;

	/**
	 * Constructor
	 * @param propName
	 * @param lblText
	 * @param dataMap
	 */
	public AbstractDataMapField(String propName, String lblText, Map<String, String> dataMap) {
		super(propName, lblText);
		this.dataMap = dataMap;
	}

	public Map<String, String> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
}
