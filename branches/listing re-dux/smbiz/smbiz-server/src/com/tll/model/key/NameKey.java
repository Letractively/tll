package com.tll.model.key;

import com.tll.model.INamedEntity;

/**
 * INameKey impl
 * @author jpk
 */
public class NameKey extends EntityKey {

	private static final long serialVersionUID = -3217664978174156618L;

	public static final String DEFAULT_FIELDNAME = INamedEntity.NAME;

	/**
	 * The name used to identify the field that holds the name.
	 */
	private String fieldName;

	/**
	 * The actual name value.
	 */
	private String name;

	/**
	 * Constructor
	 * @param entityClass
	 */
	public NameKey(Class<? extends INamedEntity> entityClass) {
		this(entityClass, null, DEFAULT_FIELDNAME);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param name
	 */
	public NameKey(Class<? extends INamedEntity> entityClass, String name) {
		this(entityClass, name, DEFAULT_FIELDNAME);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param name
	 * @param fieldName
	 */
	public NameKey(Class<? extends INamedEntity> entityClass, String name, String fieldName) {
		super(entityClass);
		setName(name);
		setFieldName(fieldName);
	}

	@Override
	public String getTypeName() {
		return "Name key";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public final String getFieldName() {
		return fieldName;
	}

	public final void setFieldName(String fieldName) {
		if(fieldName == null) throw new IllegalArgumentException("A field name must be specified");
		this.fieldName = fieldName;
	}

	@Override
	protected String keyDescriptor() {
		return "Name";
	}

	@Override
	public void clear() {
		this.name = null;
	}

	@Override
	public boolean isSet() {
		return name != null;
	}
}
