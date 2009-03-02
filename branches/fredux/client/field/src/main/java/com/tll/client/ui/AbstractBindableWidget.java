package com.tll.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.tll.client.bind.IBindingAction;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.bind.IBindable;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.bind.PropertyChangeSupport;

/**
 * AbstractBindableWidget
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @param <T> The value type
 * @author jpk
 */
public abstract class AbstractBindableWidget<T> extends Composite implements IBindableWidget<T> {

	/**
	 * The optional model.
	 */
	IBindable model;

	/**
	 * The optional action.
	 */
	private IBindingAction<T, IBindableWidget<T>> action;

	/**
	 * Optional ref to registry for message popups. <br>
	 * This allows for msg popups bound to child widgets to be managed as a
	 * flyweight.
	 */
	protected MsgPopupRegistry mregistry;

	/**
	 * Responsible for disseminating <em>property</em> change events.
	 */
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	@SuppressWarnings("unchecked")
	@Override
	public final IBindingAction getAction() {
		return action;
	}

	@SuppressWarnings("unchecked")
	@Override
	public/*final*/void setAction(IBindingAction action) {
		this.action = action;
	}

	@Override
	public final IBindable getModel() {
		return model;
	}

	@Override
	public/*final*/void setModel(IBindable model) {
		// don't spuriously re-apply the same model instance!
		if(this.model != null && model == this.model) {
			return;
		}
		Log.debug("AbstractBindableWidget.setModel() - START");
		
		final IBindable old = this.model;
		
		if(old != null) {
			Log.debug("AbstractBindableWidget - unbinding existing action..");
			action.unbind(this);
		}

		this.model = model;

		if(action != null) {
			action.set(this);
			if(isAttached() && (model != null)) {
				Log.debug("AbstractBindableWidget - re-binding existing action..");
				action.bind(this);
			}
		}
		//Log.debug("AbstractBindableWidget - firing 'model' prop change event..");
		//changeSupport.firePropertyChange(PropertyChangeType.MODEL.prop(), old, model);
	}

	@Override
	public final MsgPopupRegistry getMsgPopupRegistry() {
		return mregistry;
	}

	@Override
	public void setMsgPopupRegistry(MsgPopupRegistry mregistry) {
		this.mregistry = mregistry;
	}

	public final IPropertyChangeListener[] getPropertyChangeListeners() {
		return changeSupport.getPropertyChangeListeners();
	}

	public final void addPropertyChangeListener(IPropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}

	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(propertyName, l);
	}

	public final void removePropertyChangeListener(IPropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}

	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(propertyName, l);
	}

	@Override
	protected void onAttach() {
		Log.debug("Attaching " + toString() + "..");
	  if(action != null) {
			Log.debug("Setting action [" + action + "] on [" + this + "]..");
			action.set(this);
		}
		super.onAttach();
		//Log.debug("Firing prop change 'attach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), false, true);
	}

	@Override
	protected void onLoad() {
		Log.debug("Loading " + toString() + "..");
		super.onLoad();
		if(action != null) {
			Log.debug("Binding action [" + action + "] on [" + this + "]..");
			action.bind(this);
		}
	}

	@Override
	protected void onDetach() {
		Log.debug("Detatching " + toString() + "..");
		super.onDetach();
		if(action != null) {
			Log.debug("Unbinding action [" + action + "] from [" + this + "]..");
			action.unbind(this);
		}
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), true, false);
	}

	@Override
	public String toString() {
		return "Bound Widget";
	}
}
