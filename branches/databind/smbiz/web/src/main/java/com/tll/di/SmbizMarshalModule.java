/**
 * The Logic Lab
 * @author jpk
 * @since Jun 27, 2009
 */
package com.tll.di;

import com.tll.server.marshal.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.SmbizEntityTypeResolver;
import com.tll.server.rpc.entity.SmbizMarshalOptionsResolver;


/**
 * SmbizMarshalModule
 * @author jpk
 */
public class SmbizMarshalModule extends MarshalModule {

	@Override
	protected Class<? extends IEntityTypeResolver> getEntityTypeResolverImplType() {
		return SmbizEntityTypeResolver.class;
	}

	@Override
	protected Class<? extends IMarshalOptionsResolver> getMarshalOptionsResolverImplType() {
		return SmbizMarshalOptionsResolver.class;
	}
}
