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
