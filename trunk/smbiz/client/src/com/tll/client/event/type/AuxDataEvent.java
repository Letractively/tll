/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.AuxDataPayload;

/**
 * AuxDataEvent
 * @author jpk
 */
public class AuxDataEvent extends BaseEvent {

	private final AuxDataPayload payload;

	/**
	 * Constructor
	 * @param source
	 * @param payload
	 */
	public AuxDataEvent(Widget source, AuxDataPayload payload) {
		super(source);
		this.payload = payload;
	}

	public AuxDataPayload getPayload() {
		return payload;
	}

}
