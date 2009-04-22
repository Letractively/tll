/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.common.model;

/**
 * MutableEntityType - {@link IEntityType} impl supporting mutable properties.
 * <p>
 * <em>WARNING:</em> do not attempt to employ equals()/hashCode() since we have
 * multiple {@link IEntityType} impl types!
 * @author jpk
 */
public final class MutableEntityType implements IEntityType {

	private String entityClassName;
	private String presentationName;

	/**
	 * Constructor
	 */
	public MutableEntityType() {
		super();
	}

	/**
	 * Constructor
	 * @param entityClassName Fully qualified entity class name.
	 * @param presentationName The presentation worthy entity type name
	 */
	public MutableEntityType(String entityClassName, String presentationName) {
		super();
		setEntityClassName(entityClassName);
		setPresentationName(presentationName);
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	/**
	 * Set the fully qualified entity class name.
	 * @param entityClassName
	 */
	public void setEntityClassName(String entityClassName) {
		if(entityClassName == null) throw new IllegalArgumentException("Null entity class name");
		this.entityClassName = entityClassName;
	}

	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * Sets the presentation name.
	 * @param presentationName
	 */
	protected void setPresentationName(String presentationName) {
		if(presentationName == null) throw new IllegalArgumentException("Null presentation name");
		this.presentationName = presentationName;
	}

	@Override
	public String toString() {
		return getPresentationName();
	}
}
