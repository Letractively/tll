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
import com.tll.client.event.IEditListener;
import com.tll.client.event.ISourcesEditEvents;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.EditEvent.EditOp;
import com.tll.client.model.Model;
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
public final class EditPanel extends Composite implements ClickListener, ISourcesEditEvents {

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
	private FieldGroupPanel fieldPanel;

	/**
	 * The panel containing the edit buttons
	 */
	private final FlowPanel pnlButtonRow = new FlowPanel();

	private final Button btnSave, btnReset, btnCancel;

	private final EditListenerCollection editListeners = new EditListenerCollection();

	/**
	 * The model subject to editing.
	 */
	private Model model;

	/**
	 * Constructor
	 * @param showCancelBtn
	 */
	public EditPanel(boolean showCancelBtn) {
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

		panel.add(portal);
		panel.add(pnlButtonRow);
		initWidget(panel);
		setStyleName(STYLE_ENTITY_EDIT);
	}

	/**
	 * Constructor
	 * @param fieldPanel The panel containing the desired fields for edit.
	 * @param showCancelBtn Show the cancel button?
	 */
	public EditPanel(FieldGroupPanel fieldPanel, boolean showCancelBtn) {
		this(showCancelBtn);
		setFieldPanel(fieldPanel);
	}

	public void addEditListener(IEditListener listener) {
		editListeners.add(listener);
	}

	public void removeEditListener(IEditListener listener) {
		editListeners.remove(listener);
	}

	/**
	 * Sets or replaces the field panel.
	 * @param fieldPanel The field panel
	 */
	public void setFieldPanel(FieldGroupPanel fieldPanel) {
		if(this.fieldPanel != null && this.fieldPanel == fieldPanel) return;
		if(fieldPanel == null) {
			throw new IllegalArgumentException("A field panel must be specified.");
		}
		this.fieldPanel = fieldPanel;
		portal.setWidget(fieldPanel);
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
	 * @param model May NOT be <code>null</code>.
	 * @throws IllegalArgumentException When the entity is <code>null</code>.
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	public void applyMsgs(final List<Msg> msgs) {
		fieldPanel.getFields().handleValidationFeedback(new IValidationFeedback() {

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
		fieldPanel.neededAuxData(adr);
		return adr.size() == 0 ? null : adr;
	}

	/**
	 * Is this edit panel loaded with model data?
	 * @return true/false
	 */
	public boolean isModelLoaded() {
		return model != null;
	}

	/**
	 * Refreshes the edit panel by [re-]applying the entity model to the contained
	 * {@link FieldGroupPanel} and setting the edit button based on whether the
	 * entity is new or not.
	 */
	public void refresh() {
		if(model == null) {
			throw new IllegalStateException("No model loaded.");
		}
		btnSave.setText(model.isNew() ? "Add" : "Update");
		fieldPanel.bind(model);
		fieldPanel.render();
		pnlButtonRow.setVisible(true);
	}

	/**
	 * Resets the fields contained in this panel.
	 */
	public void reset() {
		fieldPanel.reset();
	}

	public void onClick(Widget sender) {
		if(sender == btnSave) {
			try {
				if(fieldPanel.updateModel(model)) {
					editListeners.fireEditEvent(new EditEvent(this, EditOp.SAVE, model));
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
			editListeners.fireEditEvent(new EditEvent(this, EditOp.CANCEL, model));
		}
	}
}
