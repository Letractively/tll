/**
 * The Logic Lab
 * @author jpk
 * May 11, 2008
 */
package com.tll.client.ui.edit;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * EditEvent
 * @author jpk
 */
public final class EditEvent extends EventObject {

	public enum EditOp {
		ADD,
		UPDATE,
		DELETE,
		CANCEL;

		public boolean isSave() {
			return this == ADD || this == UPDATE;
		}
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
