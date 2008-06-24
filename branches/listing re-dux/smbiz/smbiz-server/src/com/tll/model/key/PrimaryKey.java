package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Representation of primary keys within the application.
 * @author jpk
 */
public final class PrimaryKey extends EntityKey {

	private static final long serialVersionUID = 6971947122659535069L;

	private Integer id;

	/**
	 * Constructor
	 * @param e
	 */
	public PrimaryKey(IEntity e) {
		this(e.entityClass(), e.getId());
	}

	/**
	 * Constructor
	 * @param entityClass
	 */
	public PrimaryKey(Class<? extends IEntity> entityClass) {
		this(entityClass, null);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param id
	 */
	public PrimaryKey(Class<? extends IEntity> entityClass, Integer id) {
		super(entityClass);
		setId(id);
	}

	@Override
	public String getTypeName() {
		return "Primary Key";
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	@Override
	protected String keyDescriptor() {
		return "Id " + getId();
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
}
