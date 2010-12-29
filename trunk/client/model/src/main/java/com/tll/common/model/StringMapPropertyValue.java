/**
 * The Logic Lab
 */
package com.tll.common.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

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
	public StringMapPropertyValue(final String name, final PropertyMetadata metadata, final Map<String, String> map) {
		super(name, metadata);
		setStringMap(map);
	}

	@Override
	public PropertyType getType() {
		return PropertyType.STRING_MAP;
	}

	@Override
	public IPropertyValue copy() {
		return new StringMapPropertyValue(propertyName, metadata, map == null ? null : new LinkedHashMap<String, String>(
				map));
	}

	@Override
	public final Object getValue() {
		return map;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doSetValue(final Object obj) {
		if(obj == null) {
			this.map = null;
		}
		else if(obj instanceof Map) {
			setStringMap((Map<String, String>) obj);
		}
		else {
			throw new IllegalArgumentException("The value must be a String-wise Map");
		}
	}

	public Map<String, String> getStringMap() {
		return map;
	}

	private void setStringMap(final Map<String, String> map) {
		if(map == null) return;
		if(this.map == null) {
			this.map = new LinkedHashMap<String, String>();
		}
		this.map.putAll(map);
	}
}
