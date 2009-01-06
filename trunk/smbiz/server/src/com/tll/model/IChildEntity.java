package com.tll.model;

/**
 * Indicates an entities' ability to exist in a collection owned by a parent
 * entity.
 * <P>
 * the parent entity
 * @author jpk
 * @param <P>
 */
public interface IChildEntity<P extends IEntity> extends IEntity {

	/**
	 * @return the parent entity
	 */
	P getParent();

	/**
	 * @param e the parent entity
	 */
	void setParent(P e);
}
