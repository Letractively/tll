/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.SmbizEntityTypeResolver;
import com.tll.server.rpc.entity.SmbizMarshalOptionsResolver;


/**
 * SmbizMarshalModule
 * @author jpk
 */
public class SmbizMarshalModule extends MarshalModule {

	@Override
	protected void bindEntityTypeResolver() {
		bind(IEntityTypeResolver.class).to(SmbizEntityTypeResolver.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindMarshalOptionsResolver() {
		bind(IMarshalOptionsResolver.class).to(SmbizMarshalOptionsResolver.class).in(Scopes.SINGLETON);
	}
}
