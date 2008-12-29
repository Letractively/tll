package com.tll.client.ui;

import java.util.Comparator;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.tll.client.bind.IBindingAction;
import com.tll.client.bind.IPropertyChangeListener;
import com.tll.client.bind.PropertyChangeSupport;
import com.tll.client.renderer.IRenderer;

/**
 * AbstractBoundWidget
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @param <B> The bound value type
 * @param <V> The native widget value type
 * @param <M> The model type
 * @author jpk
 */
public abstract class AbstractBoundWidget<B, V, M> extends Composite implements IBoundWidget<B, V, M> {

	/**
	 * The binding action.
	 */
	private IBindingAction<IBoundWidget<B, V, M>> action;

	/**
	 * Responsible for converting a <B> type to a <V> type.
	 */
	private IRenderer<V, B> renderer;

	/**
	 * The comparator.
	 */
	private Comparator<B> comparator;

	/**
	 * The subjugated model
	 */
	private M model;

	/**
	 * The widget specific change listeners.
	 */
	private ChangeListenerCollection changeListeners;

	/**
	 * Responsible for disseminating <em>property</em> change events.
	 */
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	/**
	 * Constructor
	 */
	public AbstractBoundWidget() {
		super();
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
	protected final void fireWidgetChange() {
		changeListeners.fireChange(this);
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

	public IBindingAction<IBoundWidget<B, V, M>> getAction() {
		return action;
	}

	public void setAction(IBindingAction<IBoundWidget<B, V, M>> action) {
		this.action = action;
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

	public final void setModel(M model) {
		final Object old = getModel();
		final IBindingAction<IBoundWidget<B, V, M>> action = getAction();

		if(old != null && action != null) {
			action.unbind();
		}

		this.model = model;

		if(action != null) action.setBindable(this);

		if(isAttached() && model != null && action != null) {
			action.bind();
		}

		changeSupport.firePropertyChange("model", old, model);
	}

	public final IRenderer<V, B> getRenderer() {
		if(renderer == null) throw new IllegalStateException("No renderer specified.");
		return renderer;
	}

	public void setRenderer(IRenderer<V, B> renderer) {
		if(renderer == null) throw new IllegalArgumentException("A renderer must be specified.");
		this.renderer = renderer;
	}

	@Override
	protected void onAttach() {
		if(getAction() != null) getAction().setBindable(this);
		super.onAttach();
		changeSupport.firePropertyChange("attached", false, true);
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
		changeSupport.firePropertyChange("attached", true, false);
	}
}
