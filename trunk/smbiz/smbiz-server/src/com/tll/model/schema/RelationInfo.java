/**
 * The Logic Lab
 * @author jpk
 * Feb 20, 2008
 */
package com.tll.model.schema;


/**
 * RelationInfo - Defines key information about a defined relation (betw. 2
 * entities) in the schema.
 * @author jpk
 */
public final class RelationInfo extends AbstractSchemaProperty {

	/**
	 * Is this relation pointing to an entity or collection of entities that
	 * manage their own life-cycle?
	 */
	private final boolean reference;

	/**
	 * Constructor
	 * @param propertyType
	 * @param reference
	 */
	public RelationInfo(final PropertyType propertyType, final boolean reference) {
		super(propertyType);
		this.reference = reference;
	}

	public boolean isReference() {
		return reference;
	}
}
