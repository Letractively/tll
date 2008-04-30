package com.tll.client.listing;

import com.tll.client.data.PropKey;
import com.tll.client.util.GlobalFormat;

/**
 * Column
 * @author jpk
 */
public final class Column {

	/**
	 * Unique token to indictate this column is for row counting.
	 */
	public static final String ROW_COUNT_COL_PROP = "__rc";

	/**
	 * The UI presentable column name.
	 */
	public final String name;
	/**
	 * The prop name corres. to {@link PropKey#prop}.
	 */
	public final String prop;

	/**
	 * The format directive
	 */
	public final GlobalFormat format;

	/**
	 * Constructor
	 * @param name
	 * @param prop
	 */
	public Column(String name, String prop) {
		super();
		this.name = name;
		this.prop = prop;
		this.format = null;
	}

	/**
	 * Constructor
	 * @param name
	 * @param prop
	 */
	public Column(String name, String prop, GlobalFormat format) {
		super();
		this.name = name;
		this.prop = prop;
		this.format = format;
	}

	@Override
	public String toString() {
		return "name: " + name + ", prop: " + prop + ", format: " + format;
	}

}