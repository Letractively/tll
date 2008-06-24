/**
 * The Logic Lab
 * @author jkirton
 * Jun 24, 2008
 */
package com.tll.model.key;

import java.util.Arrays;

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
	private final String[] fieldNames;

	/**
	 * The name of this business key.
	 */
	private final String keyName;

	/**
	 * Constructor
	 * @param entityClass The entity type
	 * @param keyName The business key name
	 * @param fieldNames OGNL formatted String representing the properties of the
	 *        business key.
	 */
	public BusinessKeyDefinition(Class<? extends IEntity> entityClass, String keyName, String[] fieldNames) {
		if(entityClass == null) throw new IllegalArgumentException("An entity type must be specified.");
		if(fieldNames == null || fieldNames.length < 1) {
			throw new IllegalArgumentException("At least one property must be specified in a business key");
		}
		this.entityClass = entityClass;
		this.fieldNames = fieldNames;
		this.keyName = keyName;
	}

	/**
	 * @return the entityClass
	 */
	public Class<? extends IEntity> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return the keyName
	 */
	public String getKeyName() {
		return keyName;
	}

	/**
	 * @return the fieldNames
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final BusinessKeyDefinition other = (BusinessKeyDefinition) obj;
		if(entityClass == null) {
			if(other.entityClass != null) return false;
		}
		else if(!entityClass.equals(other.entityClass)) return false;
		if(!Arrays.equals(fieldNames, other.fieldNames)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityClass == null) ? 0 : entityClass.hashCode());
		result = prime * result + Arrays.hashCode(fieldNames);
		return result;
	}

	@Override
	public String toString() {
		return keyName.toString() + " - " + entityClass.toString();
	}
}
