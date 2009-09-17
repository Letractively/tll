/**
 * The Logic Lab
 * @author jpk
 * May 11, 2008
 */
package com.tll.client.ui.edit;

import com.google.gwt.event.shared.GwtEvent;

/**
 * EditEvent
 * @author jpk
 */
public final class EditEvent extends GwtEvent<IEditHandler> {

	public enum EditOp {
		ADD,
		UPDATE,
		DELETE,
		CANCEL;

		public boolean isSave() {
			return this == ADD || this == UPDATE;
		}
	}

	public static final Type<IEditHandler> TYPE = new Type<IEditHandler>();
	
  /**
	 * Fires a value change event on all registered handlers in the handler
	 * manager.If no such handlers exist, this method will do nothing.
	 * @param <I> the old value type
	 * @param source the source of the handlers
	 * @param op the edit operation
	 */
	public static <I> void fire(IHasEditHandlers source, EditOp op) {
		source.fireEvent(new EditEvent(op));
	}

	private final EditOp op;

	/**
	 * Constructor
	 * @param op
	 */
	EditEvent(EditOp op) {
		this.op = op;
	}

	public EditOp getOp() {
		return op;
	}

	@Override
	protected void dispatch(IEditHandler handler) {
		handler.onEdit(this);
	}

	@Override
	public GwtEvent.Type<IEditHandler> getAssociatedType() {
		return TYPE;
	}
}
