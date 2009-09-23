/**
 * The Logic Lab
 * @author jpk
 * May 11, 2008
 */
package com.tll.client.ui.edit;

import com.google.gwt.event.shared.GwtEvent;
import com.tll.client.model.ModelPropertyChangeTracker;
import com.tll.common.model.Model;

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
	 * Fires an edit event signifying a request to add.
	 * @param source the source of the handlers
	 */
	public static void fireAdd(IHasEditHandlers source) {
		source.fireEvent(new EditEvent(EditOp.ADD, null));
	}

	/**
	 * Fires an edit event signifying a request to update.
	 * @param source the source of the handlers
	 * @param changedModel the model data to update containing ONLY those
	 *        properties that were altered
	 * @see ModelPropertyChangeTracker
	 */
	public static void fireUpdate(IHasEditHandlers source, Model changedModel) {
		source.fireEvent(new EditEvent(EditOp.UPDATE, changedModel));
	}

	public static void fireDelete(IHasEditHandlers source) {
		source.fireEvent(new EditEvent(EditOp.DELETE, null));
	}

	public static void fireCancel(IHasEditHandlers source) {
		source.fireEvent(new EditEvent(EditOp.CANCEL, null));
	}

	private final EditOp op;

	private final Model changedModel;

	/**
	 * Constructor
	 * @param op
	 * @param changedModel
	 */
	private EditEvent(EditOp op, Model changedModel) {
		this.op = op;
		this.changedModel = changedModel;
	}

	public EditOp getOp() {
		return op;
	}

	public Model getChangedModel() {
		return changedModel;
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
