/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.tll.server.rpc.entity.IPersistServiceImplResolver;
import com.tll.server.rpc.entity.SmbizPersistServiceImplResolver;


/**
 * SmbizClientPersistModule
 * @author jpk
 */
public class SmbizClientPersistModule extends ClientPersistModule {

	@Override
	protected Class<? extends IPersistServiceImplResolver> getPersistServiceImplResolverType() {
		return SmbizPersistServiceImplResolver.class;
	}
}
