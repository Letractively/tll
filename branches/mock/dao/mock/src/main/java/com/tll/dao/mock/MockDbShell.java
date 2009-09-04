/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.dao.mock;

import com.google.inject.Inject;
import com.tll.dao.IDbShell;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphPopulator;


/**
 * MockDbShell
 * @author jpk
 */
public class MockDbShell implements IDbShell {

	private final EntityGraph graph;
	private final IEntityGraphPopulator builder;

	/**
	 * Constructor
	 * @param graph
	 * @param builder
	 */
	@Inject
	public MockDbShell(EntityGraph graph, IEntityGraphPopulator builder) {
		super();
		this.graph = graph;
		this.builder = builder;
		this.builder.setEntityGraph(graph);
	}

	@Override
	public boolean clear() {
		graph.clear();
		return true;
	}

	@Override
	public boolean create() {
		builder.populateEntityGraph();
		return true;
	}

	@Override
	public boolean delete() {
		clear();
		return true;
	}

	@Override
	public boolean stub() {
		builder.populateEntityGraph();
		return true;
	}

	@Override
	public void restub() {
		clear();
		stub();
	}
}
