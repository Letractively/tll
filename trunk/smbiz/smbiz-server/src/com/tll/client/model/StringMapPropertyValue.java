/**
 * The Logic Lab
 */
package com.tll.client.model;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * StringMapPropertyValue - Wraps a String/String {@link Map}.
 * @author jpk
 */
public class StringMapPropertyValue extends AbstractPropertyValue {

	private Map<String, String> map;

	/**
	 * Constructor
	 */
	public StringMapPropertyValue() {
		super();
	}

	/**
	 * Constructor
	 * @param name
	 * @param metadata
	 * @param map
	 */
	public StringMapPropertyValue(String name, PropertyMetadata metadata, Map<String, String> map) {
		super(name, metadata);
		this.map = map;
	}

	public PropertyType getType() {
		return PropertyType.STRING_MAP;
	}

	public void clear() {
		if(map != null) {
			map.clear();
		}
	}

	public IPropertyValue copy() {
		return new StringMapPropertyValue(getPropertyName(), null, map == null ? null : new HashMap<String, String>(map));
	}

	public final Object getValue() {
		return map;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object obj) {
		if(obj instanceof Map == false) {
			throw new IllegalArgumentException("The value must be a String-wise Map");
		}
		setStringMap((Map<String, String>) obj);
	}

	public Map<String, String> getStringMap() {
		return map;
	}

	public void setStringMap(Map<String, String> map) {
		this.map = map;
	}
}
