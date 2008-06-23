package com.tll.model.key;

import com.tll.key.IKey;
import com.tll.model.IEntity;

/**
 * Representation of primary keys within the application.
 * @author jpk
 */
final class PrimaryKey<E extends IEntity> extends EntityKey<E> implements IPrimaryKey<E> {

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
	public void setEntity(E entity) {
		setId(entity.getId());
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
	public int compareTo(IKey<E> o) {
		if(o instanceof PrimaryKey == false) throw new ClassCastException("The key must be a primary key to compare");
		if(!o.isSet()) throw new IllegalArgumentException("The compared key is not set");
		final Integer tid = ((PrimaryKey) o).getId();
		assert tid != null;
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
