/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.common.data.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.common.data.EntityPrototypeRequest;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;

/**
 * ICrudServiceAsync
 * @see ICrudService
 * @author jpk
 */
public interface ICrudServiceAsync {

	void getEmptyEntity(EntityPrototypeRequest request, AsyncCallback<EntityPayload> callback);

	void load(EntityLoadRequest request, AsyncCallback<EntityPayload> callback);

	void persist(EntityPersistRequest request, AsyncCallback<EntityPayload> callback);

	void purge(EntityPurgeRequest request, AsyncCallback<EntityPayload> callback);
}
