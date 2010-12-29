/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.IMarshalable;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.search.ISearch;

/**
 * ICrudService
 * @param <M> model type
 * @author jpk
 */
@RemoteServiceRelativePath(value = "rpc/crud")
public interface ICrudService<M extends IMarshalable> extends RemoteService {

	/**
	 * Loads model data.
	 * @param request The load request
	 * @return ModelPayload
	 */
	ModelPayload<M> load(LoadRequest<? extends ISearch> request);

	/**
	 * Persists model data.
	 * @param request The persist request
	 * @return ModelPayload
	 */
	ModelPayload<M> persist(PersistRequest<M> request);

	/**
	 * Purges model data.
	 * @param request The purge reqeuest
	 * @return ModelPayload
	 */
	ModelPayload<M> purge(PurgeRequest<M> request);
}
