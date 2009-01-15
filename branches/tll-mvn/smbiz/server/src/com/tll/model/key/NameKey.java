package com.tll.model.key;

import com.tll.model.INamedEntity;

/**
 * INameKey impl
 * @author jpk
 * @param <N>
 */
public class NameKey<N extends INamedEntity> extends AbstractEntityKey<N> {

	private static final long serialVersionUID = -3217664978174156618L;

	public static final String DEFAULT_FIELDNAME = INamedEntity.NAME;

	/**
	 * The name used to identify the field that holds the name.
	 */
	private String propertyName;

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
		setPropertyName(propertyName);
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

	public final String getPropertyName() {
		return propertyName;
	}

	public final void setPropertyName(String fieldName) {
		if(fieldName == null) throw new IllegalArgumentException("A field name must be specified");
		this.propertyName = fieldName;
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
