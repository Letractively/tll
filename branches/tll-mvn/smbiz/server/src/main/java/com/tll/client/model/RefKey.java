/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.model;

import com.tll.IDescriptorProvider;
import com.tll.IMarshalable;
import com.tll.model.EntityType;

/**
 * RefKey - Generic model entity proxy encapsulating the uniqueness of the
 * referenced model entity.
 * @author jpk
 */
public final class RefKey implements IMarshalable, IDescriptorProvider {

	/**
	 * Required type
	 */
	private EntityType type;

	/**
	 * Required id
	 */
	private Integer id;

	/**
	 * Optional name
	 */
	private String name;

	/**
	 * Constructor
	 */
	public RefKey() {
		super();
	}

	/**
	 * Constructor
	 * @param type
	 * @param id
	 * @param name May be <code>null</code>
	 */
	public RefKey(EntityType type, Integer id, String name) {
		super();
		setType(type);
		setId(id);
		setName(name);
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		if(type == null) {
			throw new IllegalArgumentException("A type must be specified for ref keys");
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

	public void clear() {
		type = null;
		id = null;
		name = null;
	}

	public boolean isSet() {
		return id != null && /* name != null && name.length() > 0 && */type != null;
	}

	/**
	 * @return A UI friendly String
	 */
	public String descriptor() {
		return (name != null) ? type.getName() + " '" + name + '\'' : type.getName();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		// NOTE: we always return false if the key is not set
		if(!isSet()) return false;
		if(getClass() != obj.getClass()) return false;

		final RefKey other = (RefKey) obj;

		if(!type.equals(other.type)) return false;

		if(!id.equals(other.id)) return false;

		if(name == null) {
			if(other.name != null) return false;
		}
		else if(!name.equals(other.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public final String toString() {
		return (name != null) ? type + " '" + name + "' (Id: " + id + ")" : type + " (Id: " + id + ")";
	}
}
