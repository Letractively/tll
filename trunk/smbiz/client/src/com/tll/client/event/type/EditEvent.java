/**
 * The Logic Lab
 * @author jpk
 * May 11, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;

/**
 * EditEvent
 * @author jpk
 */
public final class EditEvent extends BaseEvent {

	public enum EditOp {
		SAVE,
		DELETE,
		CANCEL;
	}

	private final EditOp op;

	/**
	 * Constructor
	 * @param source
	 * @param op
	 */
	public EditEvent(Widget source, EditOp op) {
		super(source);
		this.op = op;
	}

	public EditOp getOp() {
		return op;
	}
}
