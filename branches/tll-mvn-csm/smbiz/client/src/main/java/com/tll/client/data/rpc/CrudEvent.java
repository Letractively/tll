/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.data.rpc;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.rpc.CrudCommand.CrudOp;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityRequest;

/**
 * CrudEvent
 * @author jpk
 */
public final class CrudEvent extends EventObject {

	private final CrudOp crudOp;
	private final EntityRequest request;
	private final EntityPayload payload;

	/**
	 * Constructor
	 * @param source
	 * @param crudOp
	 * @param request
	 * @param payload
	 */
	public CrudEvent(Widget source, CrudOp crudOp, EntityRequest request, EntityPayload payload) {
		super(source);
		this.crudOp = crudOp;
		this.request = request;
		this.payload = payload;
	}

	public CrudOp getCrudOp() {
		return crudOp;
	}

	/**
	 * @return the request
	 */
	public EntityRequest getRequest() {
		return request;
	}

	public EntityPayload getPayload() {
		return payload;
	}
}
