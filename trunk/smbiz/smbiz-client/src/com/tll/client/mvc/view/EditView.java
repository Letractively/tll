/**
 * The Logic Lab
 * @author jpk Jan 5, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.data.EntityOptions;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.UnloadViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.model.CommitModelChangeHandler;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.Dispatcher;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.FieldGroupPanel;

/**
 * EditView - Dedicated base class for AbstractView impls whose sole purpose is
 * to edit a single entity.
 * @author jpk
 */
public abstract class EditView extends AbstractView implements IModelChangeListener {

	/**
	 * The Panel containing the UI edit Widgets.
	 */
	protected final EditPanel editPanel;

	/**
	 * The unique model reference.
	 */
	protected RefKey modelRef;

	/**
	 * Constructor
	 * @param fldGrpPnl The field group panel. May NOT be <code>null</code>.
	 * @param entityOptions
	 */
	public EditView(FieldGroupPanel fldGrpPnl, EntityOptions entityOptions) {
		super();
		IModelChangeHandler handler = new CommitModelChangeHandler(this);
		handler.addModelChangeListener(this);
		editPanel = new EditPanel(fldGrpPnl, entityOptions, true, handler);
		addWidget(editPanel);
	}

	public String getLongViewName() {
		return "Edit " + modelRef.toString();
	}

	@Override
	public String getShortViewName() {
		String s = modelRef.getName();
		if(s == null) {
			// fallback to the entity type
			s = modelRef.getType().getName();
		}
		return "Edit " + s;
	}

	@Override
	public final ShowViewRequest newViewRequest() {
		return new EditViewRequest(this, getViewClass(), modelRef);
	}

	@Override
	protected final void doInitialization(ViewRequestEvent viewRequest) {
		assert viewRequest instanceof EditViewRequest;
		EditViewRequest r = (EditViewRequest) viewRequest;

		Model entityGroup = r.getEntity();
		if(entityGroup == null) {
			modelRef = r.getEntityRef();
			editPanel.setEntityRef(modelRef);
		}
		else {
			modelRef = entityGroup.getRefKey();
			editPanel.setEntity(entityGroup);
		}
		assert modelRef != null : "No entity ref specified.";
	}

	@Override
	public final void refresh() {
		editPanel.refresh();
	}

	@Override
	protected void doDestroy() {
		// no-op
	}

	@Override
	public void onModelChangeEvent(ModelChangeEvent event) {
		if(event.isCanceled()) {
			Dispatcher.instance().dispatch(new UnloadViewRequest(this, getViewKey()));
		}
		else if(event.isError()) {
			// TODO necessary?
			// refresh();
		}
	}

}
