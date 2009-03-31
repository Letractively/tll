package com.tll.client.listing;

import com.tll.client.util.GlobalFormat;

/**
 * Column - Listing column definition. A {@link Column} collection dictates the
 * columns and the table cell formatting in a client-side listing.
 * @author jpk
 */
public class Column {
	
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
	 * Constructor
	 * @param name
	 */
	public Column(String name) {
		this(name, null);
	}

	/**
	 * Constructor
	 * @param name The presentation column name.
	 * @param format the format to employ for the cells in this column.
	 */
	public Column(String name, GlobalFormat format) {
		this.name = name;
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

	@Override
	public String toString() {
		return "name: " + name + ", format: " + format;
	}
}