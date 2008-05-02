package com.tll.client.listing;

import com.tll.client.util.GlobalFormat;
import com.tll.listhandler.SortColumn;

/**
 * Column - UI wise column
 * @author jpk
 */
public final class Column extends SortColumn {

	/**
	 * Unique token to indictate this column is for row counting.
	 */
	public static final String ROW_COUNT_COL_PROP = "__rc";

	/**
	 * The UI presentable column name.
	 */
	public final String name;

	/**
	 * The format directive
	 */
	public final GlobalFormat format;

	/**
	 * Constructor
	 * @param name
	 * @param prop
	 * @param parentAlias
	 */
	public Column(String name, String prop, String parentAlias) {
		this(name, prop, parentAlias, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param prop
	 * @param parentAlias
	 * @param format
	 */
	public Column(String name, String prop, String parentAlias, GlobalFormat format) {
		super(prop, parentAlias);
		this.name = name;
		this.format = format;
	}

	@Override
	public String toString() {
		return "name: " + name + ", format: " + format;
	}

}