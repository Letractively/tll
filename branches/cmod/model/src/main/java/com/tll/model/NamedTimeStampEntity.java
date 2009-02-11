package com.tll.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * @author jpk
 */
@MappedSuperclass
public abstract class NamedTimeStampEntity extends TimeStampEntity implements INamedTimeStampEntity {

	private static final long serialVersionUID = 2186964556332599921L;

	protected String name;

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Transient
	@Override
	public String descriptor() {
		return typeName() + " '" + getName() + "'";
	}
}