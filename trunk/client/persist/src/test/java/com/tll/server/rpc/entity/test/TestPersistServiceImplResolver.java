/**
 * The Logic Lab
 * @author jpk
 * @since Oct 4, 2009
 */
package com.tll.server.rpc.entity.test;

import com.tll.common.data.IModelRelatedRequest;
import com.tll.server.rpc.entity.IPersistServiceImpl;
import com.tll.server.rpc.entity.IPersistServiceImplResolver;

public class TestPersistServiceImplResolver implements IPersistServiceImplResolver {
	@Override
	public Class<? extends IPersistServiceImpl> resolve(IModelRelatedRequest request)
	throws IllegalArgumentException {
		return RpcAddressService.class;
	}
}