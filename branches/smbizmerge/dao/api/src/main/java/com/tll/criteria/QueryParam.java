/**
 * The Logic Lab
 * @author jpk
 * @since Dec 24, 2010
 */
package com.tll.criteria;

import com.tll.model.PropertyType;

/**
 * QueryParam
 * @author jpk
 */
public class QueryParam implements IQueryParam {
	
	private final String propName;
	private final PropertyType ptype;
	private final Object propVal;

	/**
	 * Constructor
	 * @param propName
	 * @param ptype
	 * @param propVal
	 */
	public QueryParam(String propName, PropertyType ptype, Object propVal) {
		super();
		this.propName = propName;
		this.ptype = ptype;
		this.propVal = propVal;
	}

	@Override
	public String getPropertyName() {
		return propName;
	}

	@Override
	public PropertyType getType() {
		return ptype;
	}

	@Override
	public Object getValue() {
		return propVal;
	}

}
