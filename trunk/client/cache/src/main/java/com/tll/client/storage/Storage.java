/**
 * The Logic Lab
 * @author jpk
 * Jul 11, 2008
 */
package com.tll.client.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Storage
 * @author jpk
 */
public abstract class Storage {

	private final Map<String, String> values = new HashMap<String, String>();

	protected Map<String, String> getValues() {
		return values;
	}

	public Storage() throws StorageException {
		load();
	}

	public String getValue(String key) {
		return values.get(key);
	}

	public void setValue(String key, String value) {
		values.put(key, value);
	}

	public abstract void save() throws StorageException;

	public abstract void load() throws StorageException;
}
