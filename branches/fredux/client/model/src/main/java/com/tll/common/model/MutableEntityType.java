/**
 * The Logic Lab
 * @author jpk Feb 11, 2009
 */
package com.tll.common.model;


/**
 * MutableEntityType
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
	 * Set the unqualified entity class name (no package name).
	 * @param entityClassName
	 */
	public void setEntityClassName(String entityClassName) {
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
		this.presentationName = presentationName;
	}
}
