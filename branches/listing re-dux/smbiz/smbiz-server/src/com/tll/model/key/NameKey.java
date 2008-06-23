package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.INamedEntity;

/**
 * INameKey impl
 * @author jpk
 */
public class NameKey<N extends INamedEntity> extends EntityKey<N> implements INameKey<N> {

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
		super(entityClass);
		setName(name);
		setFieldName(fieldName);
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
	public void setEntity(N entity) {
		super.setEntity(entity);
		entity.setName(getName());
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

	@Override
	@SuppressWarnings("unchecked")
	protected NameKey<N> clone() throws CloneNotSupportedException {
		NameKey<N> cln = (NameKey) super.clone();
		cln.entityClass = this.entityClass;
		cln.fieldName = this.fieldName;
		cln.name = this.name;
		return cln;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(IKey<N> o) {
		if(o instanceof NameKey == false) throw new ClassCastException("A key must be a name key to compare");
		if(!o.isSet()) throw new IllegalArgumentException("The comparing key is not set");
		NameKey<N> that = (NameKey) o;
		if(that.fieldName.equals(this.fieldName)) throw new IllegalArgumentException("The field names must be equal");
		return this.name.compareTo(that.name);
	}
}
