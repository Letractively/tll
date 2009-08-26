package com.tll.model;

/**
 * IChildEntity - Indicates an entities' ability to exist in a collection owned
 * by a parent entity.
 * @author jpk
 * @param <P> the parent entity type
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
