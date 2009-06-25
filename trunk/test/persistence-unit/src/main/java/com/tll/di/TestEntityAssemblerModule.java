/**
 * The Logic Lab
 * @author jpk
 * @since May 20, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityAssembler;


/**
 * TestEntityAssemblerModule
 * @author jpk
 */
public class TestEntityAssemblerModule extends EntityAssemblerModule {

	@Override
	protected void bindEntityAssembler() {
		bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
	}

}
