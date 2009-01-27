/**
 * The Logic Lab
 * @author jpk
 * Jan 23, 2009
 */
package com.tll.model;

/**
 * IEntityAssembler - Entity assembly definition.
 * @author jpk
 */
public interface IEntityAssembler {

	/**
	 * Generically assembles an entity of the given type.
	 * @param <E>
	 * @param entityType
	 * @param entityProvider Contains related entities that are wired to the
	 *        assembled entity. May be <code>null</code>.
	 * @param generate Generate an id?
	 * @return The assembled IEntity of the specified type.
	 */
	<E extends IEntity> E assembleEntity(Class<E> entityType, IEntityProvider entityProvider, boolean generate);

}