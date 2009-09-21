package com.tll.model.schema;

import java.util.Map;

/**
 * ISchemaInfo - Provides entity meta data for all defined entities.
 * @author jpk
 */
public interface ISchemaInfo {

	/**
	 * Provides a map of schema properties keyed by property name for a target entity type.
	 * @param entityClass the entity type
	 * @return the associated schema map
	 */
	Map<String, ISchemaProperty> getSchemaProperties(Class<?> entityClass);

	/**
	 * Provides the schema info for the given property for a given entity type.
	 * @param entityClass the entity type
	 * @param propertyName the property name
	 * @return the corres. schema property type
	 * @throws SchemaInfoException When the given property doesn't exist
	 */
	ISchemaProperty getSchemaProperty(Class<?> entityClass, String propertyName)
	throws SchemaInfoException;

	/**
	 * Retrieves the {@link PropertyMetadata} for the given entity property name.
	 * @param entityClass The entity type
	 * @param propertyName The entity property name
	 * @return {@link PropertyMetadata} assoc. with the given property
	 * @throws SchemaInfoException When the property is not found for the given
	 *         entity type or is not a value type property.
	 */
	PropertyMetadata getPropertyMetadata(Class<?> entityClass, String propertyName) throws SchemaInfoException;

	/**
	 * Retrieves the {@link RelationInfo} for the given entity property name.
	 * @param entityClass The entity type
	 * @param propertyName The entity property name
	 * @return the field descriptor assoc. with the given field
	 * @throws SchemaInfoException When the property is not found for the given
	 *         entity type or is not relational.
	 */
	RelationInfo getRelationInfo(Class<?> entityClass, String propertyName) throws SchemaInfoException;

	/**
	 * Retrieves the {@link NestedInfo} for the given entity property name.
	 * @param entityClass The entity type
	 * @param propertyName The entity property name
	 * @return the field descriptor assoc. with the given field
	 * @throws SchemaInfoException When the property is not found for the given
	 *         entity type or is not of nested type.
	 */
	NestedInfo getNestedInfo(Class<?> entityClass, String propertyName) throws SchemaInfoException;
}
