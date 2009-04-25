/**
 * The Logic Lab
 * @author jpk
 * @since Apr 22, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.data.rpc.IAuxDataService;
import com.tll.common.data.rpc.ICrudService;
import com.tll.server.rpc.RpcServlet;

/**
 * CrudAuxDataService - {@link ICrudService} impl.
 * @author jpk
 */
public class CrudAuxDataService extends RpcServlet implements ICrudService, IAuxDataService {
	private static final long serialVersionUID = 7648139224336273139L;

	private MEntityServiceDelegate getDelegate() {
		return (MEntityServiceDelegate) getRequestContext().getServletContext().getAttribute(MEntityServiceDelegate.KEY);
	}

	@Override
	public AuxDataPayload handleAuxDataRequest(AuxDataRequest request) {
		return getDelegate().handleAuxDataRequest(request);
	}

	@Override
	public EntityPayload load(EntityLoadRequest request) {
		return getDelegate().load(request);
	}

	@Override
	public EntityPayload persist(EntityPersistRequest request) {
		return getDelegate().persist(request);
	}

	@Override
	public EntityPayload purge(EntityPurgeRequest request) {
		return getDelegate().purge(request);
	}
}
