/**
 * The Logic Lab
 * @author jpk
 * May 11, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.Model;

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
	private final Model model;

	/**
	 * Constructor
	 * @param source
	 * @param op
	 * @param model
	 */
	public EditEvent(Widget source, EditOp op, Model model) {
		super(source);
		this.op = op;
		this.model = model;
	}

	public EditOp getOp() {
		return op;
	}

	public Model getModel() {
		return model;
	}

}
