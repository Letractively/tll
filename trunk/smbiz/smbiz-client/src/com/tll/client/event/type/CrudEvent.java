/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityRequest;

/**
 * CrudEvent
 * @author jpk
 */
public final class CrudEvent extends BaseEvent {

	private final int crudOp;
	private final EntityRequest request;
	private final EntityPayload payload;

	/**
	 * Constructor
	 * @param source
	 * @param crudOp
	 * @param payload
	 */
	public CrudEvent(Widget source, int crudOp, EntityRequest request, EntityPayload payload) {
		super(source);
		this.crudOp = crudOp;
		this.request = request;
		this.payload = payload;
	}

	public int getCrudOp() {
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
