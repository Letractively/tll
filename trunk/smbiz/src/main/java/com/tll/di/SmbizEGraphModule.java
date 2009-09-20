/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.SmbizEntityGraphBuilder;


/**
 * SmbizEGraphModule
 * @author jpk
 */
public class SmbizEGraphModule extends EGraphModule {

	/**
	 * Constructor
	 */
	public SmbizEGraphModule() {
		super();
	}

	/**
	 * Constructor
	 * @param filename
	 */
	public SmbizEGraphModule(String filename) {
		super(filename);
	}

	@Override
	protected void bindEntityGraphBuilder() {
		bind(IEntityGraphPopulator.class).to(SmbizEntityGraphBuilder.class).in(Scopes.SINGLETON);
	}

}
