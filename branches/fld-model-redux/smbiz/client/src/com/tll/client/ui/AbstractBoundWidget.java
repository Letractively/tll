/*
 * AbstractBoundWidget.java
 *
 * Created on June 14, 2007, 9:55 AM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.tll.client.ui;

import java.util.Comparator;

import com.google.gwt.user.client.ui.Composite;
import com.tll.client.bind.IAction;
import com.tll.client.bind.IBindable;
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
 * @param <A> The {@link IAction} type
 * @param <M> The model type
 * @author jpk
 */
public abstract class AbstractBoundWidget<B, V, A extends IBindingAction<IBindable>, M> extends Composite implements IBoundWidget<B, V, A, M> {

	private A action;

	/**
	 * Responsible for converting a <B> type to a <V> type.
	 */
	private IRenderer<V, B> renderer;

	private Comparator<B> comparator;

	/**
	 * The subjugated model. This is a placeholder data member.
	 */
	private M model;

	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	/**
	 * Constructor
	 */
	public AbstractBoundWidget() {
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

	public final A getAction() {
		return action;
	}

	public final void setAction(A action) {
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
		Object old = this.getModel();
		if(this.getModel() != null) {
			getAction().unbind(this);
		}

		this.model = model;

		getAction().set(this);
		if(this.isAttached() && (this.getModel() != null)) {
			getAction().bind(this);
		}
		this.changeSupport.firePropertyChange("model", old, model);
	}

	public final IRenderer<V, B> getRenderer() {
		return renderer;
	}

	public void setRenderer(IRenderer<V, B> renderer) {
		if(renderer == null) throw new IllegalArgumentException("A renderer must be specified.");
		this.renderer = renderer;
	}

	@Override
	protected void onAttach() {
		getAction().set(this);
		super.onAttach();
		this.changeSupport.firePropertyChange("attached", false, true);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		getAction().bind(this);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		getAction().unbind(this);
		this.changeSupport.firePropertyChange("attached", true, false);
	}
}
