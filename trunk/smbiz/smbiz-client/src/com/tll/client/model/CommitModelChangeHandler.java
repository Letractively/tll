package com.tll.client.model;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.event.ICrudListener;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.CrudEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.ModelChangeEvent.ModelChangeOp;
import com.tll.client.msg.Msg;

/**
 * CommitModelChangeHandler - Commits model changes via {@link CrudCommand}s.
 * @author jpk
 */
public final class CommitModelChangeHandler implements ICrudListener, IModelChangeHandler {

	private final ModelChangeListenerCollection listeners = new ModelChangeListenerCollection();

	private final Widget sourcingWidget;

	/**
	 * Constructor
	 */
	public CommitModelChangeHandler(Widget sourcingWidget) {
		super();
		this.sourcingWidget = sourcingWidget;
	}

	public void addModelChangeListener(IModelChangeListener listener) {
		listeners.add(listener);
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		listeners.remove(listener);
	}

	public void handleModelChangeCancellation(ModelChangeOp canceledOp, Model model) {
		ModelChangeEvent event = new ModelChangeEvent(sourcingWidget, canceledOp, model);
		event.setCanceled(true);
		listeners.fireOnModelChange(event);
	}

	public void handleModelAdd(Model model) {
		CrudCommand cmd = new CrudCommand(sourcingWidget);
		cmd.add(model);
		cmd.addCrudListener(this);
		cmd.execute();
	}

	public void handleModelUpdate(Model model) {
		CrudCommand cmd = new CrudCommand(sourcingWidget);
		cmd.update(model);
		cmd.addCrudListener(this);
		cmd.execute();
	}

	public void handleModelDelete(RefKey modelRef) {
		CrudCommand cmd = new CrudCommand(sourcingWidget);
		cmd.purge(modelRef);
		cmd.addCrudListener(this);
		cmd.execute();
	}

	public void onCrudEvent(CrudEvent event) {

		final boolean isError = event.getPayload().getStatus().hasErrors();
		final List<Msg> errors = event.getPayload().getStatus().getFieldMsgs();

		ModelChangeEvent mce = null;

		switch(event.getCrudOp()) {
			case ADD:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.ADDED, event.getPayload().getEntity());
				break;
			case UPDATE:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.UPDATED, event.getPayload().getEntity());
				break;
			case PURGE:
				mce = new ModelChangeEvent(event.getWidget(), ModelChangeOp.DELETED, event.getPayload().getEntityRef());
				break;
		}

		if(mce != null) {
			if(isError) mce.setErrors(errors);
			listeners.fireOnModelChange(mce);
		}
	}

}
