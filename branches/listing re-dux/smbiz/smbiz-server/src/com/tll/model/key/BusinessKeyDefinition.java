/**
 * The Logic Lab
 * @author jkirton
 * Jun 24, 2008
 */
package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * BusinessKeyDefinition - Defines the fields that makeup a particular business
 * key for a particular entity type and ascribes a name to the business key.
 * @author jpk
 */
public final class BusinessKeyDefinition implements IBusinessKeyDefinition {

	/**
	 * The entity to which the business key applies.
	 */
	private final Class<? extends IEntity> entityClass;

	/**
	 * OGNL formatted property paths representing the properties of the defined
	 * business key.
	 */
	private final String[] propertyNames;

	/**
	 * The name of this business key.
	 */
	private final String businessKeyName;

	/**
	 * Constructor
	 * @param entityClass The entity type
	 * @param businessKeyName The business key name
	 * @param propertyNames OGNL formatted String representing the properties of
	 *        the business key.
	 */
	public BusinessKeyDefinition(Class<? extends IEntity> entityClass, String businessKeyName, String[] propertyNames) {
		if(entityClass == null) throw new IllegalArgumentException("An entity type must be specified.");
		if(propertyNames == null || propertyNames.length < 1) {
			throw new IllegalArgumentException("At least one property must be specified in a business key");
		}
		this.entityClass = entityClass;
		this.propertyNames = propertyNames;
		this.businessKeyName = businessKeyName;
	}

	/**
	 * @return the entityClass
	 */
	public Class<? extends IEntity> getType() {
		return entityClass;
	}

	/**
	 * @return the keyName
	 */
	public String getBusinessKeyName() {
		return businessKeyName;
	}

	/**
	 * @return the propertyNames
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}
}
