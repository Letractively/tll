/**
 * The Logic Lab
 * @author jpk
 * Jan 31, 2009
 */
package com.tll.model.test;

/**
 * IEntityGraphPopulator - Populates a targeted {@link EntityGraph} instance.
 * @author jpk
 */
public interface IEntityGraphPopulator {

	/**
	 * @return The entity graph that was previously set or <code>null</code> if no
	 *         graph was previously set.
	 */
	EntityGraph getEntityGraph();

	/**
	 * Sets the entity graph that will be subject to populating.
	 * @param graph the targeted graph
	 */
	void setEntityGraph(EntityGraph graph);

	/**
	 * Populates the entity graph.
	 * @throws IllegalStateException When the operation fails
	 */
	void populateEntityGraph() throws IllegalStateException;
}
