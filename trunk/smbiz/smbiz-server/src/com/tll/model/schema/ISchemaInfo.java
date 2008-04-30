package com.tll.model.schema;

import java.util.Map;

import com.tll.model.IEntity;

/**
 * ISchemaInfo - Provides entity meta data for all defined entities.
 * @author jpk
 */
public interface ISchemaInfo {

	/**
	 * @param entityClass
	 * @return serviceMap of field descriptor keyed by field names
	 * @throws SchemaInfoException if invalid entity class specified
	 */
	Map<String, ISchemaProperty> getAllSchemaProperties(Class<? extends IEntity> entityClass) throws SchemaInfoException;

	/**
	 * @param entityClass
	 * @return array of field names for the given entity class
	 * @throws SchemaInfoException
	 */
	String[] getSchemaPropertyNames(Class<? extends IEntity> entityClass) throws SchemaInfoException;

	/**
	 * Retrieves the {@link FieldData} for the given entity property name.
	 * @param entityClass The entity type
	 * @param propertyName The entity property name
	 * @return {@link FieldData} assoc. with the given property
	 * @throws SchemaInfoException When the property is not found for the given
	 *         entity type
	 */
	FieldData getFieldData(Class<? extends IEntity> entityClass, String propertyName) throws SchemaInfoException;

	/**
	 * Retrieves the {@link RelationInfo} for the given entity property name.
	 * @param entityClass The entity type
	 * @param propertyName The entity property name
	 * @return the field descriptor assoc. with the given field
	 * @throws SchemaInfoException When the property is not found for the given
	 *         entity type
	 */
	RelationInfo getRelationInfo(Class<? extends IEntity> entityClass, String propertyName) throws SchemaInfoException;
}
