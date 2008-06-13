package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Representation of primary keys within the application.
 * @author jpk
 */
class PrimaryKey<E extends IEntity> extends EntityKey<E> implements IPrimaryKey<E> {

	static final long serialVersionUID = 6971947122659535069L;

	private static final String[] FIELDS = new String[] { IEntity.PK_FIELDNAME };

	private final Class<E> entityClass;

	/**
	 * Constructor
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	public PrimaryKey(E e) {
		this((Class) e.entityClass(), e.getId());
	}

	/**
	 * Constructor
	 * @param entityClass
	 */
	public PrimaryKey(Class<E> entityClass) {
		this(entityClass, null);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param id
	 */
	public PrimaryKey(Class<E> entityClass, Integer id) {
		super();
		this.entityClass = entityClass;
		setId(id);
	}

	public Class<E> getType() {
		return entityClass;
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setId(Integer id) {
		setValue(0, id);
	}

	public Integer getId() {
		return (Integer) getValue(0);
	}

	public void setEntity(E entity) {
		setId(entity.getId());
	}

	public String descriptor() {
		return getType().getSimpleName() + "(Id: " + getId() + ")";
	}
}
