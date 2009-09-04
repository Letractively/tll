/**
 * The Logic Lab
 * @author jpk
 * @since Mar 19, 2009
 */
package com.tll.client.listing;

import com.tll.client.util.GlobalFormat;
import com.tll.model.schema.IPropertyNameProvider;

/**
 * PropertyBoundColumn - A table column with a bound property name.
 * @author jpk
 */
public class PropertyBoundColumn extends Column implements IPropertyNameProvider {

	private final String propName;
	
	/**
	 * The data-store specific parent alias mainly called on when a [remote] named
	 * query is involved in fetching listing data as this is when aliasing is
	 * necessary for query column disambiguation.
	 */
	private final String parentAlias;

	/**
	 * Constructor
	 * @param name
	 * @param format
	 * @param propName the bound OGNL compliant property name
	 */
	public PropertyBoundColumn(String name, GlobalFormat format, String propName) {
		this(name, format, propName, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param format
	 * @param propName the bound OGNL compliant property name
	 * @param parentAlias the parent alias in the server side data retrieval query
	 */
	public PropertyBoundColumn(String name, GlobalFormat format, String propName, String parentAlias) {
		super(name, format);
		this.propName = propName;
		this.parentAlias = parentAlias;
	}

	/**
	 * Constructor
	 * @param name
	 * @param propName the bound OGNL compliant property name
	 */
	public PropertyBoundColumn(String name, String propName) {
		this(name, null, propName, null);
	}

	/**
	 * Constructor
	 * @param name
	 * @param propName the bound OGNL compliant property name
	 * @param parentAlias the parent alias in the server side data retrieval query
	 */
	public PropertyBoundColumn(String name, String propName, String parentAlias) {
		this(name, null, propName, parentAlias);
	}

	@Override
	public String getPropertyName() {
		return propName;
	}

	
	public String getParentAlias() {
		return parentAlias;
	}
}
