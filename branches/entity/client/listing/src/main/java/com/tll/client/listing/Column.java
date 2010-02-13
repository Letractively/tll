package com.tll.client.listing;

import com.tll.client.util.GlobalFormat;
import com.tll.schema.IPropertyNameProvider;

/**
 * Column - Listing column definition. A {@link Column} collection dictates the
 * columns and the table cell formatting in a client-side listing.
 * @author jpk
 */
public class Column implements IPropertyNameProvider {

	public static final Column ROW_COUNT_COLUMN = new Column("#");

	/**
	 * The UI presentable column name.
	 */
	private final String name;

	/**
	 * The format directive
	 */
	private final GlobalFormat format;

	/**
	 * Optional property name (dot notation).
	 */
	private final String propertyName;

	/**
	 * Optional data-store specific parent alias mainly called on when a [remote]
	 * named query is involved in fetching listing data as this is when aliasing
	 * is necessary for query column disambiguation.
	 */
	private final String parentAlias;

	/**
	 * Constructor - No property binding
	 * @param name
	 */
	public Column(String name) {
		this(name, null, null, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param propertyName
	 */
	public Column(String name, String propertyName) {
		this(name, null, propertyName, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param propertyName
	 * @param parentAlias
	 */
	public Column(String name, String propertyName, String parentAlias) {
		this(name, null, propertyName, parentAlias);
	}

	/**
	 * Constructor - No property binding
	 * @param name The presentation column name.
	 * @param format the format to employ for the cells in this column.
	 */
	public Column(String name, GlobalFormat format) {
		this(name, format, null, null);
	}

	/**
	 * Constructor - Property binding
	 * @param name
	 * @param format
	 * @param propertyName
	 */
	public Column(String name, GlobalFormat format, String propertyName) {
		this(name, format, propertyName, null);
	}

	/**
	 * Constructor - Property binding
	 * @param name
	 * @param format
	 * @param propertyName
	 * @param parentAlias
	 */
	public Column(String name, GlobalFormat format, String propertyName, String parentAlias) {
		super();
		this.name = name;
		this.format = format;
		this.propertyName = propertyName;
		this.parentAlias = parentAlias;
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
	 * @return the property path that binds to model data
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the parent alias used in conjunction with the proerty name on the server
	 */
	public String getParentAlias() {
		return parentAlias;
	}

	@Override
	public String toString() {
		return "name: " + name + ", format: " + format;
	}
}