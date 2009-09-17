/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.SmbizPersistServiceImplResolver;


/**
 * SmbizClientPersistModule
 * @author jpk
 */
public class SmbizClientPersistModule extends ClientPersistModule {

	@Override
	protected void bindPersistServiceImplResolver() {
		bind(IPersistServiceImplResolver.class).to(SmbizPersistServiceImplResolver.class).in(Scopes.SINGLETON);
	}

}
