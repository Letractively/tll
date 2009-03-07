/**
 * The Logic Lab
 * @author jpk
 * Mar 6, 2009
 */
package com.tll.client.convert;

import java.util.Map;

/**
 * DataMapConverter
 * @author jpk
 * @param <I> inbound type
 */
public class DataMapConverter<I> implements IConverter<String, I> {

	/**
	 * The data map of presentation worthy tokens keyed by the value.
	 */
	private Map<I, String> data;

	/**
	 * Constructor
	 */
	public DataMapConverter() {
	}

	/**
	 * Constructor
	 * @param data
	 */
	public DataMapConverter(Map<I, String> data) {
		super();
		setData(data);
	}

	@Override
	public String convert(I in) throws IllegalArgumentException {
		if(!data.containsKey(in)) {
			throw new IllegalArgumentException();
		}
		return data.get(in);
	}

	/**
	 * Set or reset the field data. <br>
	 * @param data Map of value/name pairs keyed by <em>value</em> where each key
	 *        holds the token for use in the ui.
	 */
	public void setData(Map<I, String> data) {
		this.data = data;
	}

	/**
	 * Adds a single value/name data item.
	 * @param name the presentation name
	 * @param value the field value
	 */
	public void addDataItem(String name, I value) {
		data.put(value, name);
	}

	/**
	 * Removes a single data item
	 * @param value the field value data item value to remove
	 */
	public void removeDataItem(I value) {
		data.remove(value);
	}

	/*
	public String getToken(I value) {
		return data.get(value);
	}

	public I getDataValue(String key) {
		for(final I val : data.keySet()) {
			if(data.get(val).equals(key)) {
				return val;
			}
		}
		throw new IllegalArgumentException();
	}
	*/
}
