package com.tll.model.key;

import com.tll.model.INamedEntity;

/**
 * Key for named entities.
 * @author jpk
 */
public class NameKey<N extends INamedEntity> extends BusinessKey<N> implements INameKey<N> {

	private static final long serialVersionUID = -3217664978174156618L;
	private static final String DEFAULT_FIELDNAME = INamedEntity.NAME;
	private String fieldName;
	private Class<N> entityClass;

	/**
	 * Constructor
	 * @param entityClass
	 */
	public NameKey(Class<N> entityClass) {
		this(entityClass, null, DEFAULT_FIELDNAME);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param name
	 */
	public NameKey(Class<N> entityClass, String name) {
		this(entityClass, name, DEFAULT_FIELDNAME);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param name
	 * @param fieldName
	 */
	public NameKey(Class<N> entityClass, String name, String fieldName) {
		super();
		this.entityClass = entityClass;
		setName(name);
		setFieldName(fieldName);
	}

	public final Class<N> getType() {
		return entityClass;
	}

	/*
	 * This is the default impl. Sub-classes should override this method if there
	 * are additional fields.
	 */
	@Override
	protected String[] getFields() {
		return new String[] { DEFAULT_FIELDNAME };
	}

	public String getName() {
		return (String) getValue(0);
	}

	public void setName(String name) {
		setValue(0, name);
	}

	public final String getFieldName() {
		return fieldName;
	}

	public final void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setEntity(N entity) {
		entity.setName(getName());
	}

	@Override
	protected String keyDescriptor() {
		return "Name";
	}

}
