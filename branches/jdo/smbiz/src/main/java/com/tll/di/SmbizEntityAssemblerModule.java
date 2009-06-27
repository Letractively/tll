/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.SmbizEntityAssembler;


/**
 * SmbizEntityAssemblerModule
 * @author jpk
 */
public class SmbizEntityAssemblerModule extends EntityAssemblerModule {

	@Override
	protected void bindEntityAssembler() {
		bind(IEntityAssembler.class).to(SmbizEntityAssembler.class).in(Scopes.SINGLETON);
	}

}
