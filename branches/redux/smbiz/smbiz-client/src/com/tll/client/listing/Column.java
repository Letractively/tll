package com.tll.client.listing;

import com.tll.client.IMarshalable;
import com.tll.client.util.GlobalFormat;
import com.tll.listhandler.SortColumn;

/**
 * Column - UI wise column that exists soley on the client side.
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
	 * Constructor - This is not used and is for GWT compiler compliance since we
	 * are (spuriously) extending {@link SortColumn} which is {@link IMarshalable}.
	 */
	public Column() {
		this(null, null, null);
	}

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