/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.SmbizEntityTypeResolver;
import com.tll.server.rpc.entity.SmbizMarshalOptionsResolver;
import com.tll.server.rpc.entity.SmbizPersistServiceImplResolver;


/**
 * SmbizClientPersistModule
 * @author jpk
 */
public class SmbizClientPersistModule extends ClientPersistModule {

	@Override
	protected void bindEntityTypeResolver() {
		bind(IEntityTypeResolver.class).to(SmbizEntityTypeResolver.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindMarshalOptionsResolver() {
		bind(IMarshalOptionsResolver.class).to(SmbizMarshalOptionsResolver.class).in(Scopes.SINGLETON);
	}

	@Override
	protected void bindPersistServiceImplResolver() {
		bind(IPersistServiceImplResolver.class).to(SmbizPersistServiceImplResolver.class).in(Scopes.SINGLETON);
	}

}
