/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.IMarshalable;
import com.tll.common.data.LoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;
import com.tll.common.search.ISearch;

/**
 * ICrudServiceAsync
 * @param <M> model type
 * @see ICrudService
 * @author jpk
 */
public interface ICrudServiceAsync<M extends IMarshalable> {

	void load(LoadRequest<? extends ISearch> request, AsyncCallback<ModelPayload<M>> callback);

	void persist(PersistRequest<M> request, AsyncCallback<ModelPayload<M>> callback);

	void purge(PurgeRequest<M> request, AsyncCallback<ModelPayload<M>> callback);
}
