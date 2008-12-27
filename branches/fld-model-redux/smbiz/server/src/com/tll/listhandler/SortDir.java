package com.tll.listhandler;

import com.tll.client.IMarshalable;
import com.tll.util.INameValueProvider;

/**
 * Sort direction enumeration.
 * @author jpk
 */
public enum SortDir implements INameValueProvider<String>, IMarshalable {
	ASC("Ascending", "asc"),
	DESC("Descending", "desc");

	private final String name;
	private final String sqlclause;

	SortDir(String name, String sqlclause) {
		this.name = name;
		this.sqlclause = sqlclause;
	}

	public String getName() {
		return name;
	}

	public String getSqlclause() {
		return sqlclause;
	}

	public String getValue() {
		return toString();
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
