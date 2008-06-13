package com.tll.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Named entity abstract class
 * @author jpk
 */
@MappedSuperclass
public abstract class NamedEntity extends EntityBase implements INamedEntity {

	protected String name;

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