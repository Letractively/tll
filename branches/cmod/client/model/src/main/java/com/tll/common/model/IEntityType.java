/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.common.model;


/**
 * IEntityType - Defines a way to hold an entity type in a generic way providing
 * a descriptive name for that entity type.
 * @author jpk
 */
public interface IEntityType {

	/**
	 * @return The fully qualified class name of the entity.
	 */
	String getClassName();
	
	/**
	 * @return A presentation worthy display name for the entity type.
	 */
	String getPresentationName();
}
