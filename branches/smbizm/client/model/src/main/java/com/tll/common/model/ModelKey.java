/**
 * The Logic Lab
 * @author jpk
 * @since Mar 14, 2009
 */
package com.tll.common.model;

import com.tll.key.IKey;

/**
 * ModelKey
 * @author jpk
 */
@SuppressWarnings("serial")
public class ModelKey implements IKey<Model> {

	/**
	 * The entity type.
	 */
	private IEntityType type;

	/**
	 * The entity id.
	 */
	private Integer id;

	/**
	 * The entity name.
	 */
	private String name;

	/**
	 * Constructor
	 */
	public ModelKey() {
		super();
	}

	/**
	 * Constructor
	 * @param type the entity type
	 * @param id the entity id
	 * @param name the optional entity name
	 */
	public ModelKey(IEntityType type, Integer id, String name) {
		setEntityType(type);
		setId(id);
		setName(name);
	}

	@Override
	public Class<Model> getType() {
		return Model.class;
	}

	public IEntityType getEntityType() {
		return type;
	}

	public void setEntityType(IEntityType type) {
		if(type == null) {
			throw new IllegalArgumentException("Null entity type");
		}
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void clear() {
		type = null;
		id = null;
		name = null;
	}

	@Override
	public boolean isSet() {
		return type != null && id != null;
	}

	@Override
	public String descriptor() {
		return isSet() ? (name != null) ? type.getPresentationName() + " '" + name + '\'' : type.getPresentationName()
				: "-unset-";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		final String ecn = type == null ? null : type.getEntityClassName();
		result = prime * result + ((ecn == null) ? 0 : ecn.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final ModelKey other = (ModelKey) obj;
		if(id == null) {
			if(other.id != null) return false;
		}
		else if(!id.equals(other.id)) return false;
		if(type == null) {
			if(other.type != null) return false;
		}
		final String ecn = type == null ? null : type.getEntityClassName();
		final String otherEcn = other.type == null ? null : other.type.getEntityClassName();
		if(ecn == null) {
			if(otherEcn != null) return false;
		}
		else if(!ecn.equals(otherEcn)) return false;
		return true;
	}

	@Override
	public String toString() {
		return (name != null) ? type + " '" + name + "' (Id: " + id + ")" : type + " (Id: " + id + ")";
	}
}