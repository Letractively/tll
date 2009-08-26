/**
 * The Logic Lab
 * @author jpk
 * @since May 20, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.TestPersistenceUnitEntityAssembler;
import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.model.key.SimplePrimaryKeyGenerator;


/**
 * TestEntityAssemblerModule
 * @author jpk
 */
public class TestEntityAssemblerModule extends ModelModule {

	@Override
	protected void bindEntityAssembler() {
		bind(IEntityAssembler.class).to(TestPersistenceUnitEntityAssembler.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindPrimaryKeyGenerator() {
		bind(IPrimaryKeyGenerator.class).to(SimplePrimaryKeyGenerator.class).in(Scopes.SINGLETON);
	}

}
