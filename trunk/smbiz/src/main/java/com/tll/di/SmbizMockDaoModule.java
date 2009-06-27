/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.SmbizEntityGraphBuilder;


/**
 * SmbizMockDaoModule
 * @author jpk
 */
public class SmbizMockDaoModule extends MockDaoModule {

	@Override
	protected void bindEntityGraphBuilder() {
		bind(IEntityGraphBuilder.class).to(SmbizEntityGraphBuilder.class).in(Scopes.SINGLETON);
	}

}
