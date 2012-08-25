package com.tll.client.listing;

import com.tll.IPropertyNameProvider;
import com.tll.client.util.GlobalFormat;

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
	private String name;

	/**
	 * The data format directive.
	 */
	private GlobalFormat format;

	/**
	 * Optional CSS style class.
	 */
	private String style;

	/**
	 * Optional property name (dot notation).
	 */
	private String propertyName;

	/**
	 * Optional data-store specific parent alias mainly called on when a [remote]
	 * named query is involved in fetching listing data as this is when aliasing
	 * is necessary for query column disambiguation.
	 */
	private String parentAlias;
	
	/**
	 * Is this column sortable;
	 */
	private boolean sortable;

	/**
	 * Ignore case when sorting?
	 */
	private boolean ignoreCase;

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
		this(name, null, propertyName, null, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param propertyName
	 * @param parentAlias
	 */
	public Column(String name, String propertyName, String parentAlias) {
		this(name, null, propertyName, parentAlias, null);
	}

	/**
	 * Constructor - No property binding
	 * @param name The presentation column name.
	 * @param format the format to employ for the cells in this column.
	 */
	public Column(String name, GlobalFormat format) {
		this(name, format, null, null, null);
	}

	/**
	 * Constructor - Property binding
	 * @param name
	 * @param format
	 * @param propertyName
	 */
	public Column(String name, GlobalFormat format, String propertyName) {
		this(name, format, propertyName, null, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param format
	 * @param propertyName
	 * @param parentAlias
	 */
	public Column(String name, GlobalFormat format, String propertyName, String parentAlias) {
		this(name, format, propertyName, parentAlias, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param format
	 * @param propertyName
	 * @param parentAlias
	 * @param style
	 */
	public Column(String name, GlobalFormat format, String propertyName, String parentAlias, String style) {
		this(name, format, style, propertyName, parentAlias, false, false);
	}

	/**
	 * Constructor - all properties
	 * @param name
	 * @param format
	 * @param style
	 * @param propertyName
	 * @param parentAlias
	 * @param sortable
	 * @param ignoreCase
	 */
	public Column(String name, GlobalFormat format, String style, String propertyName, String parentAlias,
			boolean sortable, boolean ignoreCase) {
		super();
		this.name = name;
		this.format = format;
		this.style = style;
		this.propertyName = propertyName;
		this.parentAlias = parentAlias;
		this.sortable = sortable;
		this.ignoreCase = ignoreCase;
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
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the parent alias used in conjunction with the proerty name on the
	 *         server
	 */
	public String getParentAlias() {
		return parentAlias;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public boolean isSortable() {
		return sortable;
	}
	
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Column [name=").append(name).append(", format=").append(format).append(", style=").append(style)
				.append(", propertyName=").append(propertyName).append(", parentAlias=").append(parentAlias)
				.append(", ignoreCase=").append(ignoreCase).append("]");
		return builder.toString();
	}

}