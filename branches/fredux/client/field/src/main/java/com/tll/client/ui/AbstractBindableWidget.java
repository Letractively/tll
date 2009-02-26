package com.tll.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
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
	 * Responsible for disseminating <em>property</em> change events.
	 */
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	/**
	 * Constructor
	 */
	public AbstractBindableWidget() {
		super();
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
		// if(getAction() != null) getAction().setBindable(this);
		super.onAttach();
		// changeSupport.firePropertyChange(PROPERTY_ATTACHED, false, true);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// if(getAction() != null) getAction().bind();
	}

	@Override
	protected void onDetach() {
		Log.debug("Detatching " + toString() + "..");
		super.onDetach();
		// if(getAction() != null) getAction().unbind();
		// changeSupport.firePropertyChange(PROPERTY_ATTACHED, true, false);
	}

	@Override
	public String toString() {
		return "Bound Widget";
	}
}
