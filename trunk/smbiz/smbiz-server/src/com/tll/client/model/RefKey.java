/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.model;

import com.tll.client.IMarshalable;
import com.tll.model.EntityType;

/**
 * RefKey - Generic proxy for an Object having an id, type and optionally a
 * name.
 * @author jpk
 */
public class RefKey implements IMarshalable {

	/**
	 * Required type
	 */
	protected EntityType type;
	/**
	 * Required id
	 */
	protected Integer id;
	/**
	 * Optional name
	 */
	protected String name;

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
		this.type = type;
		this.id = id;
		this.name = name;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
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
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof RefKey)) {
			return false;
		}

		final RefKey key = (RefKey) obj;
		return type.equals(key.type) && id.equals(key.id);
	}

	@Override
	public int hashCode() {
		return (id == null ? 0 : 27 * id.hashCode()) + (type == null ? 0 : 29 * type.hashCode());
	}

	@Override
	public final String toString() {
		return (name != null) ? type + " '" + name + "' (Id: " + id + ")" : type + " (Id: " + id + ")";
	}
}
