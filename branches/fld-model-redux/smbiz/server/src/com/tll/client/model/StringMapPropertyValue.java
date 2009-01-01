/**
 * The Logic Lab
 */
package com.tll.client.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * StringMapPropertyValue - Wraps a String/String {@link Map}.
 * @author jpk
 */
public class StringMapPropertyValue extends AbstractPropertyValue {

	private LinkedHashMap<String, String> map;

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
		setStringMap(map);
	}

	public PropertyType getType() {
		return PropertyType.STRING_MAP;
	}

	@Override
	protected void doClear() {
		if(map != null) {
			map.clear();
		}
	}

	public IPropertyValue copy() {
		return new StringMapPropertyValue(getPropertyName(), null, map == null ? null : new LinkedHashMap<String, String>(
				map));
	}

	public final Object getValue() {
		return map;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doSetValue(Object obj) {
		if(obj instanceof Map == false) {
			throw new IllegalArgumentException("The value must be a String-wise Map");
		}
		setStringMap((Map<String, String>) obj);
	}

	public Map<String, String> getStringMap() {
		return map;
	}

	private void setStringMap(Map<String, String> map) {
		if(this.map == null) {
			this.map = new LinkedHashMap<String, String>();
		}
		this.map.putAll(map);
	}
}
