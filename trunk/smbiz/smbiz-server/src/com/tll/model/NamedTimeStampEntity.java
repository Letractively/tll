package com.tll.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * @author jpk
 */
@MappedSuperclass
public abstract class NamedTimeStampEntity extends TimeStampEntity implements INamedTimeStampEntity {

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

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("name", getName());
	}
}
