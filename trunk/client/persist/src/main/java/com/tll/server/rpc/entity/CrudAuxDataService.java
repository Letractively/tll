/**
 * The Logic Lab
 * @author jpk
 * @since Apr 22, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.AuxDataPayload;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.rpc.IAuxDataService;
import com.tll.common.data.rpc.ICrudService;
import com.tll.server.rpc.RpcServlet;

/**
 * CrudAuxDataService - {@link ICrudService} impl.
 * @author jpk
 */
public class CrudAuxDataService extends RpcServlet implements ICrudService, IAuxDataService {
	private static final long serialVersionUID = 7648139224336273139L;

	private PersistServiceDelegate getDelegate() {
		return (PersistServiceDelegate) getRequestContext().getServletContext().getAttribute(PersistServiceDelegate.KEY);
	}

	@Override
	public AuxDataPayload handleAuxDataRequest(AuxDataRequest request) {
		return getDelegate().loadAuxData(request);
	}

	@Override
	public ModelPayload load(LoadRequest<?> request) {
		return getDelegate().load(request);
	}

	@Override
	public ModelPayload persist(PersistRequest request) {
		return getDelegate().persist(request);
	}

	@Override
	public ModelPayload purge(PurgeRequest request) {
		return getDelegate().purge(request);
	}
}
