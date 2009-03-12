/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.mock;

/**
 * IEntityGraphBuilder - Responsible for building {@link EntityGraph} instances.
 * @author jpk
 */
public interface IEntityGraphBuilder {

	/**
	 * @return A new {@link EntityGraph} instance.
	 * @throws IllegalStateException When the build operation fails
	 */
	EntityGraph buildEntityGraph() throws IllegalStateException;
}
