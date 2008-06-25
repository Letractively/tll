package com.tll.model.key;

import java.util.Arrays;

import com.tll.client.model.IPropertyValue;
import com.tll.model.IEntity;

/**
 * Abstract base class for all business keys in the application.
 * @author jpk
 */
public final class BusinessKey extends EntityKey implements IBusinessKeyDefinition {

	private static final long serialVersionUID = 2415120120614040086L;

	private final String businessKeyName;
	private IPropertyValue[] propertyValues;

	/**
	 * Constructor
	 * @param entityClass
	 * @param businessKeyName
	 * @param propertyValues
	 */
	public BusinessKey(Class<? extends IEntity> entityClass, String businessKeyName, IPropertyValue[] propertyValues) {
		super(entityClass);
		this.businessKeyName = businessKeyName;
		this.propertyValues = propertyValues;
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
		if(propertyValues == null) return null;
		final String[] pnames = new String[propertyValues.length];
		for(int i = 0; i < pnames.length; ++i) {
			pnames[i] = propertyValues[i].getPropertyName();
		}
		return pnames;
	}

	private int fieldIndex(String fieldName) {
		for(int i = 0; i < propertyValues.length; ++i) {
			String fname = propertyValues[i].getPropertyName();
			if(fname != null && fname.equals(fieldName)) return i;
		}
		return -1;
	}

	public Object getFieldValue(String fieldName) {
		final int index = fieldIndex(fieldName);
		return (index == -1) ? null : propertyValues[index].getValue();
	}

	public void setFieldValue(String fieldName, Object value) {
		final int index = fieldIndex(fieldName);
		if(index != -1) {
			propertyValues[index].setValue(value);
		}
	}

	@Override
	public void clear() {
		if(propertyValues != null) {
			for(IPropertyValue pv : propertyValues) {
				pv.setValue(null);
			}
		}
	}

	@Override
	public boolean isSet() {
		for(IPropertyValue pv : propertyValues) {
			if(pv == null || pv.getValue() == null) return false;
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
