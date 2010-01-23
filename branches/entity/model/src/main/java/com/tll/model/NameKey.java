package com.tll.model;

import com.tll.key.AbstractKey;

/**
 * NameKey - Simple entity key that holds an entity name and also identifies the
 * field by which that name is retrieved from the entity.
 * @author jpk
 */
public class NameKey extends AbstractKey {

	private static final long serialVersionUID = -3217664978174156618L;

	public static final String DEFAULT_FIELDNAME = INamedEntity.NAME;

	/**
	 * The name used to identify the field that holds the name.
	 */
	private String nameProperty;

	/**
	 * The actual name value.
	 */
	private String name;

	/**
	 * Constructor
	 * @param entityClass
	 */
	public NameKey(Class<?> entityClass) {
		this(entityClass, null, DEFAULT_FIELDNAME);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param name
	 */
	public NameKey(Class<?> entityClass, String name) {
		this(entityClass, name, DEFAULT_FIELDNAME);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param name
	 * @param propertyName
	 */
	public NameKey(Class<?> entityClass, String name, String propertyName) {
		super(entityClass);
		setName(name);
		setNameProperty(propertyName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The name of the property that identifies the name.
	 */
	public final String getNameProperty() {
		return nameProperty;
	}

	/**
	 * Sets the name of the property that identifies the name.
	 * @param nameProperty
	 */
	public final void setNameProperty(String nameProperty) {
		if(nameProperty == null) throw new IllegalArgumentException("A field name must be specified");
		this.nameProperty = nameProperty;
	}

	@Override
	public String descriptor() {
		return getNameProperty() + ": " + getName();
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
