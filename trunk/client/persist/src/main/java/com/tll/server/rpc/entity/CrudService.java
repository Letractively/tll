/**
 * The Logic Lab
 * @author jpk
 * @since Apr 22, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.ModelDataPayload;
import com.tll.common.data.ModelDataRequest;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.data.rpc.IModelDataService;
import com.tll.common.data.rpc.ICrudService;
import com.tll.server.rpc.RpcServlet;

/**
 * {@link ICrudService} impl.
 * @author jpk
 */
public class CrudService extends RpcServlet implements ICrudService, IModelDataService {
	private static final long serialVersionUID = 7648139224336273139L;

	private PersistServiceDelegate getDelegate() {
		return (PersistServiceDelegate) getRequestContext().getServletContext().getAttribute(PersistServiceDelegate.KEY);
	}

	@Override
	public ModelDataPayload handleModelDataRequest(ModelDataRequest request) {
		return getDelegate().loadModelData(request);
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
