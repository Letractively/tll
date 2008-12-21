/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.EntityFetchPrototypeRequest;
import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityPersistRequest;
import com.tll.client.data.EntityPurgeRequest;

/**
 * ICrudServiceAsync
 * @see ICrudService
 * @author jpk
 */
public interface ICrudServiceAsync {

	void getEmptyEntity(EntityFetchPrototypeRequest request, AsyncCallback<EntityPayload> callback);

	void load(EntityLoadRequest request, AsyncCallback<EntityPayload> callback);

	void persist(EntityPersistRequest request, AsyncCallback<EntityPayload> callback);

	void purge(EntityPurgeRequest request, AsyncCallback<EntityPayload> callback);
}
