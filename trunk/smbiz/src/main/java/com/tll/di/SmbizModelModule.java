/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.model.IEntityAssembler;
import com.tll.model.SmbizEntityAssembler;

/**
 * SmbizModelModule
 * @author jpk
 */
public class SmbizModelModule extends ModelModule {

	@Override
	protected void bindEntityAssembler() {
		bind(IEntityAssembler.class).to(SmbizEntityAssembler.class).in(Scopes.SINGLETON);
	}
}
