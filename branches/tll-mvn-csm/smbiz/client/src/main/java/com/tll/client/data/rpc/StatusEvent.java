/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.data.rpc;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;
import com.tll.common.data.Status;

/**
 * StatusEvent
 * @author jpk
 */
public class StatusEvent extends EventObject {

	private final Status status;

	/**
	 * Constructor
	 * @param source
	 * @param status
	 */
	public StatusEvent(Widget source, Status status) {
		super(source);
		assert status != null;
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

}
