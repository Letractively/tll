package com.tll.model;


/**
 * Named entity abstract class
 * @author jpk
 */
//@PersistenceCapable
public abstract class NamedEntity extends EntityBase implements INamedEntity {

	private static final long serialVersionUID = -2428890910891561540L;

	//@Persistent
	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String descriptor() {
		return typeName() + " '" + getName() + "'";
	}
}