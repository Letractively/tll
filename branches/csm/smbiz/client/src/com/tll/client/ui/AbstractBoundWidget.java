package com.tll.client.ui;

import java.util.Comparator;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.tll.client.bind.IBindable;
import com.tll.client.bind.IBindingAction;
import com.tll.client.bind.IPropertyChangeListener;
import com.tll.client.bind.PropertyChangeSupport;
import com.tll.client.convert.IConverter;

/**
 * AbstractBoundWidget
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @param <B> The bound value type
 * @param <V> The native widget value type
 * @param <M> The model type
 * @author jpk
 */
public abstract class AbstractBoundWidget<B, V, M extends IBindable> extends Composite implements IBoundWidget<B, V, M> {

	/**
	 * The binding action.
	 */
	private IBindingAction<? extends IBoundWidget<B, V, M>> action;

	/**
	 * Responsible for converting a <B> type to a <V> type.
	 */
	private IConverter<V, B> converter;

	/**
	 * The comparator.
	 */
	private Comparator<B> comparator;

	/**
	 * The subjugated model.
	 */
	private M model;

	/**
	 * The widget specific change listeners.
	 */
	private ChangeListenerCollection changeListeners;

	/**
	 * Responsible for disseminating <em>property</em> change events.
	 */
	protected PropertyChangeSupport changeSupport;

	/**
	 * Constructor
	 */
	public AbstractBoundWidget() {
		super();
	}

	public final IBindingAction<? extends IBoundWidget<B, V, M>> getAction() {
		return action;
	}

	public final void setAction(IBindingAction<? extends IBoundWidget<B, V, M>> action) {
		this.action = action;
	}

	public final IConverter<V, B> getConverter() {
		return converter;
	}

	public final void setConverter(IConverter<V, B> converter) {
		this.converter = converter;
	}

	public final Comparator<B> getComparator() {
		return comparator;
	}

	public final void setComparator(Comparator<B> comparator) {
		this.comparator = comparator;
	}

	public final M getModel() {
		return model;
	}

	@SuppressWarnings("unchecked")
	public final void setModel(M model) {
		final Object old = getModel();

		if(old == this.model) return; // TODO verify if this is ok to do

		final IBindingAction action = getAction();

		if(old != null && action != null) {
			action.unbind();
		}

		this.model = model;

		if(action != null) action.setBindable(this);

		if(/*isAttached() && */model != null && action != null) {
			action.bind();
		}

		changeSupport.firePropertyChange(PROPERTY_MODEL, old, model);
	}

	public final void addChangeListener(ChangeListener listener) {
		if(changeListeners == null) {
			changeListeners = new ChangeListenerCollection();
		}
		changeListeners.add(listener);
	}

	public final void removeChangeListener(ChangeListener listener) {
		if(changeListeners != null) {
			changeListeners.remove(listener);
		}
	}

	/**
	 * Fires a change event for subscribed {@link ChangeListener}s.
	 */
	protected final void fireChangeListeners() {
		changeListeners.fireChange(this);
	}

	protected void setPropertyChangeSupport(PropertyChangeSupport changeSupport) {
		if(changeSupport != null && (this.changeSupport != null && this.changeSupport.hasAnyListeners())) {
			throw new IllegalStateException(toString() + " already references a property change support reference");
		}
		this.changeSupport = changeSupport;
	}

	private void ensureChangeSupportAggregated() throws IllegalStateException {
		if(changeSupport == null) {
			throw new IllegalStateException("No aggregated property change support set");
		}
	}

	public final IPropertyChangeListener[] getPropertyChangeListeners() {
		ensureChangeSupportAggregated();
		return changeSupport.getPropertyChangeListeners();
	}

	public final void addPropertyChangeListener(IPropertyChangeListener l) {
		ensureChangeSupportAggregated();
		changeSupport.addPropertyChangeListener(l);
	}

	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		ensureChangeSupportAggregated();
		changeSupport.addPropertyChangeListener(propertyName, l);
	}

	public final void removePropertyChangeListener(IPropertyChangeListener l) {
		ensureChangeSupportAggregated();
		changeSupport.removePropertyChangeListener(l);
	}

	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener l) {
		ensureChangeSupportAggregated();
		changeSupport.removePropertyChangeListener(propertyName, l);
	}

	@Override
	public String toString() {
		return "Bound Widget";
	}

	// This is driven in setModel()!!!!!
	/*
	@Override
	protected void onAttach() {
		if(getAction() != null) getAction().setBindable(this);
		super.onAttach();
		changeSupport.firePropertyChange(PROPERTY_ATTACHED, false, true);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(getAction() != null) getAction().bind();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if(getAction() != null) getAction().unbind();
		changeSupport.firePropertyChange(PROPERTY_ATTACHED, true, false);
	}
	*/
}
