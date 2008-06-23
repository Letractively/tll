package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.IEntity;

/**
 * Representation of primary keys within the application.
 * @author jpk
 */
public final class PrimaryKey<E extends IEntity> extends EntityKey<E> implements IPrimaryKey<E> {

	private static final long serialVersionUID = 6971947122659535069L;

	private Integer id;

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
		super(entityClass);
		setId(id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	@Override
	protected String keyDescriptor() {
		return "Primary Key";
	}

	@Override
	public void clear() {
		this.id = null;
	}

	@Override
	public boolean isSet() {
		return id != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final PrimaryKey other = (PrimaryKey) obj;
		if(!typeCompatible(other.entityClass)) return false;
		if(id == null) {
			if(other.id != null) return false;
		}
		else if(!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((entityClass == null) ? 0 : entityClass.toString().hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(IKey<E> o) {
		if(o instanceof PrimaryKey == false) throw new ClassCastException("The key must be a primary key to compare");
		final PrimaryKey other = (PrimaryKey) o;
		if(!isSet() || !o.isSet() || !typeCompatible(other.entityClass))
			throw new IllegalArgumentException("The compare keys are not set or are not type compatible");
		final Integer tid = other.getId();
		return id.compareTo(tid);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected PrimaryKey<E> clone() throws CloneNotSupportedException {
		PrimaryKey<E> cln = (PrimaryKey) super.clone();
		cln.entityClass = this.entityClass;
		cln.id = id;
		return cln;
	}
}
