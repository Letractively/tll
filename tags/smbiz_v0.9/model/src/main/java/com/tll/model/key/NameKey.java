package com.tll.model.key;

import com.tll.model.INamedEntity;

/**
 * NameKey - Simple entity key that holds an entity name and also identifies the
 * field by which that name is retrieved from the entity.
 * @param <N> The named entity type
 * @author jpk
 */
public class NameKey<N extends INamedEntity> extends AbstractEntityKey<N> {

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
	 * @param propertyName
	 */
	public NameKey(Class<N> entityClass, String name, String propertyName) {
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
