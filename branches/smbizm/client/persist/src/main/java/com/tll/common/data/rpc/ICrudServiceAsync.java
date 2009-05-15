/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.data.PersistRequest;
import com.tll.common.data.PurgeRequest;

/**
 * ICrudServiceAsync
 * @see ICrudService
 * @author jpk
 */
public interface ICrudServiceAsync {

	void load(EntityLoadRequest request, AsyncCallback<ModelPayload> callback);

	void persist(PersistRequest request, AsyncCallback<ModelPayload> callback);

	void purge(PurgeRequest request, AsyncCallback<ModelPayload> callback);
}
