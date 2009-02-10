package com.tll.client.listing;

import com.tll.common.util.GlobalFormat;

/**
 * Column - Listing column definition. A {@link Column} collection dictates the
 * columns and the table cell formatting in a client-side listing.
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
	private final String name;

	/**
	 * The format directive
	 */
	private final GlobalFormat format;

	/**
	 * The OGNL compliant property name this column "points" to.
	 */
	private final String propertyName;

	/**
	 * The data-store specific parent alias mainly called on when a [remote] named
	 * query is involved in fetching listing data as this is when aliasing is
	 * necessary for query column disambiguation.
	 */
	private final String parentAlias;

	/**
	 * Constructor
	 * @param name The presentation column name.
	 * @param propertyName The underlying property name of this column
	 */
	public Column(String name, String propertyName) {
		this(name, propertyName, null, null);
	}

	/**
	 * Constructor
	 * @param name The presentation column name.
	 * @param propertyName The underlying property name of this column
	 * @param parentAlias The parent alias (usu. used when data fetching is driven
	 *        by named queries)
	 */
	public Column(String name, String propertyName, String parentAlias) {
		this(name, propertyName, parentAlias, null);
	}

	/**
	 * Constructor
	 * @param name The presentation column name.
	 * @param propertyName The underlying property name of this column
	 * @param parentAlias The parent alias (usu. used when data fetching is driven
	 *        by named queries)
	 * @param format The column display format
	 */
	public Column(String name, String propertyName, String parentAlias, GlobalFormat format) {
		this.name = name;
		this.propertyName = propertyName;
		this.parentAlias = parentAlias;
		this.format = format;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the format
	 */
	public GlobalFormat getFormat() {
		return format;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the parentAlias
	 */
	public String getParentAlias() {
		return parentAlias;
	}

	@Override
	public String toString() {
		return "name: " + name + ", format: " + format;
	}
}