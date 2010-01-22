package com.tll.model;

import com.tll.key.AbstractKey;

/**
 * PrimaryKey - {@link IPrimaryKey} impl intended to support global transactions
 * (as opposed to transactions limited to "entity groups") and datastore primary
 * keys of numeric long type.
 * @author jpk
 */
// TODO re-name to GlobalLongPrimaryKey
public final class PrimaryKey extends AbstractKey implements IPrimaryKey {

	private static final long serialVersionUID = 6971947122659535069L;

	private Long id;

	/**
	 * Constructor
	 * @param entityClass
	 */
	public PrimaryKey(Class<?> entityClass) {
		this(entityClass, null);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param id
	 */
	public PrimaryKey(Class<?> entityClass, Long id) {
		super(entityClass);
		setId(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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
	public String descriptor() {
		return "Id: " + getId();
	}

	@Override
	public String toString() {
		return descriptor();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!super.equals(obj)) return false;
		if(getClass() != obj.getClass()) return false;
		PrimaryKey other = (PrimaryKey) obj;
		if(id == null) {
			if(other.id != null) return false;
		}
		else if(!id.equals(other.id)) return false;
		return true;
	}

}
