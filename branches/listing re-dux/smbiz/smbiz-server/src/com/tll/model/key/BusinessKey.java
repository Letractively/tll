package com.tll.model.key;

import java.util.Arrays;

import com.tll.client.model.IPropertyValue;

/**
 * Abstract base class for all business keys in the application.
 * @author jpk
 */
public final class BusinessKey extends EntityKey implements IBusinessKeyDefinition {

	private static final long serialVersionUID = 2415120120614040086L;

	private final String businessKeyName;
	private final String[] propertyNames;
	private IPropertyValue[] propertyValues;

	/**
	 * Constructor
	 * @param def The business key definition
	 */
	public BusinessKey(IBusinessKeyDefinition def) {
		super(def.getType());
		this.businessKeyName = def.getBusinessKeyName();
		this.propertyNames = def.getPropertyNames();
		clear();
	}

	/**
	 * Constructor
	 * @param def The business key definition
	 * @param propertyValues The propertyValues array
	 */
	public BusinessKey(IBusinessKeyDefinition def, IPropertyValue[] propertyValues) {
		this(def);
		copyValues(propertyValues);
	}

	public String getBusinessKeyName() {
		return businessKeyName;
	}

	@Override
	protected String keyDescriptor() {
		return getBusinessKeyName();
	}

	public String getTypeName() {
		return "Business Key";
	}

	/**
	 * @return The field names
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}

	private void copyValues(IPropertyValue[] values) {
		for(int i = 0; i < this.propertyValues.length; ++i) {
			this.propertyValues[i] = values[i];
		}
	}

	private int fieldIndex(String fieldName) {
		for(int i = 0; i < propertyNames.length; ++i) {
			String fname = propertyNames[i];
			if(fname != null && fname.equals(fieldName)) return i;
		}
		return -1;
	}

	public Object getFieldValue(String fieldName) {
		final int index = fieldIndex(fieldName);
		return (index == -1) ? null : propertyValues[index];
	}

	public void setFieldValue(String fieldName, IPropertyValue value) {
		final int index = fieldIndex(fieldName);
		if(index != -1) {
			propertyValues[index] = value;
		}
	}

	@Override
	public void clear() {
		this.propertyValues = new IPropertyValue[propertyNames.length];
	}

	@Override
	public boolean isSet() {
		for(Object obj : propertyValues) {
			if(obj == null) return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final BusinessKey other = (BusinessKey) obj;
		if(!typeCompatible(other.entityClass)) return false;
		if(businessKeyName == null) {
			if(other.businessKeyName != null) return false;
		}
		else if(!businessKeyName.equals(other.businessKeyName)) return false;
		if(!Arrays.equals(propertyValues, other.propertyValues)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityClass == null) ? 0 : entityClass.toString().hashCode());
		result = prime * result + ((businessKeyName == null) ? 0 : businessKeyName.hashCode());
		result = prime * result + Arrays.hashCode(propertyValues);
		return result;
	}

	@Override
	public String toString() {
		return getEntityType() + " " + getBusinessKeyName();
	}
}
