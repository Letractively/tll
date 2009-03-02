package com.tll.client.bind;

import com.tll.client.ui.IBindableWidget;

/**
 * IBindingAction - Encapsulates bi-directional binding between two bindable
 * targets. The operations performed upon {@link #execute()} are implmentation
 * dependent.
 * @param <T> the value type
 * @param <BW> the bindable widget type
 * @author jpk
 */
public interface IBindingAction<T, BW extends IBindableWidget<T>> extends IAction {
	
	/**
	 * Sets (adds) the given widget to this binding action.
	 * @param widget the widget to set
	 */
	void set(BW widget);

	/**
	 * Establishes the bindings. <br>
	 * NOTE: A bound widget must be set.
	 * @param widget the widget to bind
	 */
	void bind(BW widget);

	/**
	 * Unbinds what is currently bound performing necessary clean up.
	 * @param widget the widget to ubind
	 */
	void unbind(BW widget);
}
