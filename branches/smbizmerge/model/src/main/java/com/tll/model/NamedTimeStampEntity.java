package com.tll.model;


/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * @author jpk
 */
public abstract class NamedTimeStampEntity extends TimeStampEntity implements INamedTimeStampEntity {

	private static final long serialVersionUID = 2186964556332599921L;

	protected String name;

	/**
	 * @return the entity name.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@Override
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public String descriptor() {
		return typeDesc() + " '" + getName() + "'";
	}
}
