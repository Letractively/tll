/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.field;

import java.util.List;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.event.type.ModelChangeEvent.ModelChangeOp;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.CSS;
import com.tll.client.ui.FocusCommand;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.ValidationException;

/**
 * EditPanel - Composite panel targeting a {@link FlowPanel} whose children
 * consist of a {@link ScrollPanel} containing a {@link FieldGroupPanel} and
 * another {@link FlowPanel} containing edit buttons. The {@link ScrollPanel}
 * enables the the {@link FieldGroupPanel} content to always be navigable and
 * keeps the edit and cancel buttons in constant position.
 * @author jpk
 */
public final class EditPanel extends Composite implements ClickListener {

	/**
	 * The style name for {@link EditPanel}s.
	 */
	private static final String STYLE_ENTITY_EDIT = "entityEdit";

	/**
	 * The button row style
	 */
	private static final String STYLE_BTN_ROW = "btnRow";

	/**
	 * The composite's target widget
	 */
	private final FlowPanel panel = new FlowPanel();

	/**
	 * This panel contains the {@link FieldGroupPanel}.
	 */
	// private final ScrollPanel portal = new ScrollPanel();
	private final SimplePanel portal = new SimplePanel();

	/**
	 * Contains the actual edit fields.
	 */
	private final FieldGroupPanel fldGrpPnl;

	/**
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave, btnReset, btnCancel;

	/**
	 * The ref to the entity subject to editing.
	 */
	private RefKey entityRef;

	/**
	 * The entity subject to editing.
	 */
	private Model entity;

	private IModelChangeHandler modelChangeHandler;

	/**
	 * Constructor
	 * @param fldGrpPnl The panel containing the desired fields for edit.
	 * @param showCancelBtn Show the cancel button?
	 */
	public EditPanel(FieldGroupPanel fldGrpPnl, boolean showCancelBtn) {

		if(fldGrpPnl == null) {
			throw new IllegalArgumentException("A field panel must be specified.");
		}
		this.fldGrpPnl = fldGrpPnl;

		pnlButtonRow.setStyleName(STYLE_BTN_ROW);
		// hide the button row until initialized
		pnlButtonRow.setVisible(false);

		btnSave = new Button("", this);
		pnlButtonRow.add(btnSave);

		btnReset = new Button("Reset", this);
		pnlButtonRow.add(btnReset);

		if(showCancelBtn) {
			btnCancel = new Button("Cancel", this);
			pnlButtonRow.add(btnCancel);
		}
		else {
			btnCancel = null;
		}

		portal.setStyleName(CSS.PORTAL);
		portal.add(fldGrpPnl);

		panel.add(portal);
		panel.add(pnlButtonRow);
		initWidget(panel);
		setStyleName(STYLE_ENTITY_EDIT);
	}

	/**
	 * Sets the model change handler which handles model related data acquisition
	 * and data change requests.
	 * @param modelChangeHandler The model change handler
	 */
	public void setModelChangeHandler(IModelChangeHandler modelChangeHandler) {
		this.modelChangeHandler = modelChangeHandler;
	}

	private void ensureModelHandlerSet() throws IllegalStateException {
		if(modelChangeHandler == null) {
			throw new IllegalStateException("No model handler set");
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// portal.addScrollListener(MsgManager.instance);
		if(btnCancel != null) {
			DeferredCommand.addCommand(new FocusCommand(btnCancel, true));
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		// portal.removeScrollListener(MsgManager.instance);
	}

	public void addClickListener(ClickListener listener) {
		if(btnCancel != null) {
			btnCancel.addClickListener(listener);
		}
	}

	public void removeClickListener(ClickListener listener) {
		if(btnCancel != null) {
			btnCancel.removeClickListener(listener);
		}
	}

	/**
	 * Sets the entity.
	 * @param entity May NOT be <code>null</code>.
	 * @throws IllegalArgumentException When the entity is <code>null</code>.
	 */
	public void setEntity(Model entity) {
		assert entity != null;
		setEntityRef(entity.getRefKey());
		this.entity = entity;
	}

	public void setEntityRef(RefKey entityRef) {
		if(entityRef == null || !entityRef.isSet()) {
			throw new IllegalArgumentException("Invalid entity ref key");
		}
		this.entityRef = entityRef;
		this.entity = null;
	}

	public void applyMsgs(final List<Msg> msgs) {
		fldGrpPnl.getFields().handleValidationFeedback(new IValidationFeedback() {

			public List<Msg> getValidationMessages() {
				return msgs;
			}

		});
	}

	/**
	 * @return An {@link AuxDataRequest} instance containing the needed aux data
	 *         or <code>null</code> if no aux data is needed.<br>
	 *         <strong>NOTE: </strong>This method does <em>not</em> check the
	 *         aux data cache.
	 */
	public AuxDataRequest getNeededAuxData() {
		AuxDataRequest adr = new AuxDataRequest();
		fldGrpPnl.neededAuxData(adr);
		return adr.size() == 0 ? null : adr;
	}

	/**
	 * Refreshes the edit panel by [re-]applying the entity model to the contained
	 * {@link FieldGroupPanel} and setting the edit button based on whether the
	 * entity is new or not.
	 */
	public void refresh() {
		ensureModelHandlerSet();

		// fetch entity from server?
		if(entity == null) {
			if(entityRef == null || !entityRef.isSet()) {
				throw new IllegalStateException("No valid entity ref specified");
			}
			modelChangeHandler.handleModelFetch(entityRef);
			return;
		}

		if(!modelChangeHandler.handleAuxDataFetch()) {
			btnSave.setText(entity.isNew() ? "Add" : "Update");
			fldGrpPnl.bind(entity);
			fldGrpPnl.render();
			pnlButtonRow.setVisible(true);
		}

	}

	/**
	 * Resets the fields contained in this panel.
	 */
	public void reset() {
		fldGrpPnl.reset();
	}

	public void onClick(Widget sender) {
		ensureModelHandlerSet();
		if(sender == btnSave) {
			try {
				if(fldGrpPnl.updateModel(entity)) {
					// handle the model change
					modelChangeHandler.handleModelUpdate(entity);
				}
				else {
					MsgManager.instance.post(true, new Msg("No edits detected.", MsgLevel.WARN), Position.CENTER, this, -1, true)
							.show();
				}
			}
			catch(ValidationException e) {
				// no-op
			}
		}
		else if(sender == btnReset) {
			reset();
		}
		else if(sender == btnCancel) {
			modelChangeHandler.handleModelChangeCancellation(entity.isNew() ? ModelChangeOp.ADDED : ModelChangeOp.UPDATED,
					entity);
		}
	}
}
