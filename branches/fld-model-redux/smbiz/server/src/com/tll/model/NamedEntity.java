package com.tll.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Named entity abstract class
 * @author jpk
 */
@MappedSuperclass
public abstract class NamedEntity extends EntityBase implements INamedEntity {

	private static final long serialVersionUID = -2428890910891561540L;

	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	@Override
	public String descriptor() {
		return typeName() + " '" + getName() + "'";
	}
}