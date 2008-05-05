/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.Status;
import com.tll.client.data.rpc.AuxDataCommand;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.IRpcListener;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.event.type.RpcEvent;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.CSS;
import com.tll.client.ui.FocusCommand;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.validate.ValidationException;

/**
 * EditPanel - Composite panel targeting a {@link FlowPanel} whose children
 * consist of a {@link ScrollPanel} containing a {@link FieldGroupPanel} and
 * another {@link FlowPanel} containing edit buttons. The {@link ScrollPanel}
 * enables the the {@link FieldGroupPanel} content to always be navigable and
 * keeps the edit and cancel buttons in constant position.
 * @author jpk
 */
public final class EditPanel extends Composite implements ICrudListener, IRpcListener, ClickListener, SourcesClickEvents {

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
	 * Used for committing the edits. May be <code>null</code>.
	 */
	private final EntityOptions entityOptions;

	/**
	 * Optional provision to specify needed aux data.
	 */
	private final AuxDataRequest auxDataRequest;

	/**
	 * The internal {@link CrudCommand} for committing edits.
	 */
	private final CrudCommand crudCmd = new CrudCommand(this);

	/**
	 * The ref to the entity subject to editing.
	 */
	private RefKey entityRef;

	/**
	 * The entity subject to editing.
	 */
	private Model entity;

	/**
	 * Constructor
	 * @param fldGrpPnl The panel containing the desired fields for edit.
	 * @param showCancelBtn Show the cancel button?
	 * @param entityOptions
	 */
	public EditPanel(FieldGroupPanel fldGrpPnl, EntityOptions entityOptions, boolean showCancelBtn) {
		crudCmd.addCrudListener(this);

		assert fldGrpPnl != null;
		this.fldGrpPnl = fldGrpPnl;

		this.entityOptions = entityOptions;

		// build the aux data request
		AuxDataRequest adr = new AuxDataRequest();
		fldGrpPnl.neededAuxData(adr);
		this.auxDataRequest = adr;

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

	/**
	 * Refreshes the edit panel by [re-]applying the entity model to the contained
	 * {@link FieldGroupPanel} and setting the edit button based on whether the
	 * entity is new or not.
	 */
	public void refresh() {

		// fetch entity from server?
		if(entity == null) {
			if(entityRef == null || !entityRef.isSet()) {
				throw new IllegalStateException("No valid entity ref specified");
			}
			crudCmd.load(entityRef);
			crudCmd.setEntityOptions(entityOptions);
			crudCmd.setAuxDataRequest(auxDataRequest);
			crudCmd.execute();
			return;
		}

		// do we need any aux data from the server?
		AuxDataRequest adr = AuxDataCache.instance().filterRequest(auxDataRequest);
		if(adr != null) {
			final AuxDataCommand adc = new AuxDataCommand(this, adr);
			adc.addRpcListener(this);
			adc.execute();
			return;
		}

		btnSave.setText(entity.isNew() ? "Add" : "Update");
		fldGrpPnl.bind(entity);
		fldGrpPnl.render();
		pnlButtonRow.setVisible(true);
	}

	public void onCrudEvent(CrudEvent event) {
		switch(event.getCrudOp()) {
			case RECIEVE_EMPTY_ENTITY:
			case LOAD:
				entity = event.getPayload().getEntity();
				refresh();
				break;

			case ADD:
			case UPDATE:
			case PURGE: {
				Status status = event.getPayload().getStatus();
				if(status.hasErrors()) {
					fldGrpPnl.fields.handleValidationFeedback(new ValidationException(status.getFieldMsgs()));
				}
				else {
					// re-bind the updated entity
					fldGrpPnl.bind(entity);
				}
				break;
			}
		}
	}

	/**
	 * This means an aux data request came back successfully
	 */
	public void onRpcEvent(RpcEvent event) {
		if(!event.getPayload().hasErrors()) {
			refresh();
		}
	}

	public void onClick(Widget sender) {
		if(sender == btnSave) {
			try {
				if(fldGrpPnl.updateModel(entity)) {
					// send to server
					crudCmd.persist(entity);
					crudCmd.execute();
				}
				else {
					MsgManager.instance
							.post(true, new Msg("No edits detected.", MsgLevel.WARN), Position.CENTER, this, -1, false).show();
				}
			}
			catch(ValidationException e) {

			}
		}
		else if(sender == btnReset) {
			fldGrpPnl.reset();
		}
	}
}
