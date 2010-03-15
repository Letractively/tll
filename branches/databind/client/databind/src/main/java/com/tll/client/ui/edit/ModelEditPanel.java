/**
 * The Logic Lab
 * @author jpk
 * @since Mar 14, 2010
 */
package com.tll.client.ui.edit;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.model.ModelChangeTracker;
import com.tll.client.ui.Position;
import com.tll.client.ui.field.AbstractBindableFieldPanel;
import com.tll.client.ui.field.NoChangesException;
import com.tll.client.ui.msg.Msgs;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * An edit panel around a bindable field panel providing model data in edit
 * events.
 * @author jpk
 */
public class ModelEditPanel extends AbstractEditPanel<IModelEditContent, AbstractBindableFieldPanel<?>> {

	/**
	 * Constructor
	 * @param fieldPanel
	 * @param showCancelBtn
	 * @param showDeleteBtn
	 * @param showResetBtn
	 */
	public ModelEditPanel(AbstractBindableFieldPanel<?> fieldPanel, boolean showCancelBtn, boolean showDeleteBtn,
			boolean showResetBtn) {
		super(fieldPanel, showCancelBtn, showDeleteBtn, showResetBtn);
		fieldPanel.setModelChangeTracker(new ModelChangeTracker());
	}

	public final Model getModel() {
		return fieldPanel.getModel();
	}

	public void setModel(Model model) {
		Log.debug("ModelEditPanel.setModel() - START");
		fieldPanel.setModel(model);
		if(model != null) {
			setEditMode(model.isNew());
			/*
			// deferred attachment to guaranting model and aux data is available
			if(fieldPanel.getParent() == null) {
				Log.debug("EditPanel.setModel() attaching fieldPanel..");
				getPortal().add(fieldPanel);
			}
			*/
		}
		Log.debug("ModelEditPanel.setModel() - END");
	}

	@Override
	protected IModelEditContent getEditContent() {
		try {
			Log.debug("EditPanel - Saving..");
			fieldPanel.updateModel();
			return new IModelEditContent() {
				
				@Override
				public Model getModel() {
					return fieldPanel.getModel();
				}
				
				@Override
				public Model getChangedModel() {
					return fieldPanel.getChangedModel();
				}
			};
		}
		catch(final NoChangesException e) {
			// no field edits were made
			Msgs.post(new Msg("No changes detected.", MsgLevel.INFO), this, Position.CENTER, 3000, true);
		}
		catch(final ValidationException e) {
			// turn on incremental validation after first pass
			fieldPanel.getFieldGroup().validateIncrementally(true);
		}
		catch(final Exception e) {
			String emsg = e.getMessage();
			if(emsg == null) {
				emsg = e.getClass().toString();
			}
			assert emsg != null;
			Log.error(emsg);
		}
		return null;
	}

}
