/**
 * The Logic Lab
 * @author jpk
 * Apr 23, 2008
 */
package com.tll.model.schema;


/**
 * AbstractSchemaProperty
 * @author jpk
 */
public class AbstractSchemaProperty implements ISchemaProperty {

	private final PropertyType propertyType;

	/**
	 * Constructor
	 * @param propertyType
	 */
	protected AbstractSchemaProperty(final PropertyType propertyType) {
		super();
		if(propertyType == null) {
			throw new IllegalArgumentException("A property type must be specified.");
		}
		this.propertyType = propertyType;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

}
