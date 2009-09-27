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
	 * @param addedModel The model to be added
	 */
	public static void fireAdd(IHasEditHandlers source, Model addedModel) {
		source.fireEvent(new EditEvent(EditOp.ADD, addedModel, null));
	}

	/**
	 * Fires an edit event signifying a request to update.
	 * @param source the source of the handlers
	 * @param editedModel the model containing all original properties reflecting all edit alterations
	 * @param changedModel the model data to update containing ONLY those
	 *        properties that were altered
	 * @see ModelPropertyChangeTracker
	 */
	public static void fireUpdate(IHasEditHandlers source, Model editedModel, Model changedModel) {
		source.fireEvent(new EditEvent(EditOp.UPDATE, editedModel, changedModel));
	}

	public static void fireDelete(IHasEditHandlers source) {
		source.fireEvent(new EditEvent(EditOp.DELETE, null, null));
	}

	public static void fireCancel(IHasEditHandlers source) {
		source.fireEvent(new EditEvent(EditOp.CANCEL, null, null));
	}

	private final EditOp op;

	private final Model editedModel;

	private final Model changedModel;

	/**
	 * Constructor
	 * @param op
	 * @param editedModel The entire model with edits
	 * @param changedModel Sub-set of the original model containing only those
	 *        properties that were altered. Only specified when updating.
	 */
	private EditEvent(EditOp op, Model editedModel, Model changedModel) {
		this.op = op;
		this.editedModel = editedModel;
		this.changedModel = changedModel;
	}

	public EditOp getOp() {
		return op;
	}

	/**
	 * @return The edited model
	 */
	public Model getEditedModel() {
		return editedModel;
	}

	/**
	 * @return A sub-set of the original model containing only those properties
	 *         that were altered. This property is specified only when the edit
	 *         mode is {@link EditOp#UPDATE}.
	 */
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
