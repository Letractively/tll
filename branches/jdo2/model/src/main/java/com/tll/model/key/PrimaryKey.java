package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Representation of primary keys within the application.
 * @author jpk
 * @param <E>
 */
public final class PrimaryKey<E extends IEntity> extends AbstractEntityKey<E> {

	private static final long serialVersionUID = 6971947122659535069L;

	private long id;

	/**
	 * Constructor
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public PrimaryKey(E entity) {
		this((Class<E>) entity.entityClass(), entity.getId());
	}

	/**
	 * Constructor
	 * @param entityClass
	 */
	public PrimaryKey(Class<E> entityClass) {
		this(entityClass, -1);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param id
	 */
	public PrimaryKey(Class<E> entityClass, long id) {
		super(entityClass);
		setId(id);
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	@Override
	protected String keyDescriptor() {
		return "Id " + getId();
	}

	@Override
	public void clear() {
		this.id = -1;
	}

	@Override
	public boolean isSet() {
		return id != -1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final PrimaryKey other = (PrimaryKey) obj;
		if(!typeCompatible(other.entityClass)) return false;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((entityClass == null) ? 0 : entityClass.toString().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return descriptor();
	}

}
