package com.tll.model.key;

/**
 * Abstract base class for all business keys in the application.
 * @author jpk
 */
public final class BusinessKey extends EntityKey {

	private static final long serialVersionUID = 2415120120614040086L;

	private final String name;
	private final String[] fieldNames;
	private Object[] values;

	/**
	 * Constructor
	 * @param entityClass
	 * @param fieldNames
	 */
	protected BusinessKey(IBusinessKeyDefinition def) {
		super(def.getEntityClass());
		this.name = def.getKeyName();
		this.fieldNames = def.getFieldNames();
		clear();
	}

	/**
	 * @return The field names
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	private int fieldIndex(String fieldName) {
		for(int i = 0; i < fieldNames.length; ++i) {
			String fname = fieldNames[i];
			if(fname != null && fname.equals(fieldName)) return i;
		}
		return -1;
	}

	public Object getFieldValue(String fieldName) {
		final int index = fieldIndex(fieldName);
		return (index == -1) ? null : values[index];
	}

	public void setFieldValue(String fieldName, Object value) {
		final int index = fieldIndex(fieldName);
		if(index != -1) {
			values[index] = value;
		}
	}

	@Override
	protected String keyDescriptor() {
		return name;
	}

	@Override
	public void clear() {
		this.values = new Object[fieldNames.length];
	}

	@Override
	public boolean isSet() {
		for(Object obj : values) {
			if(obj == null) return false;
		}
		return true;
	}
}
