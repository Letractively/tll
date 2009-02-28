package com.tll.client.bind;

import com.tll.client.ui.IBindableWidget;

/**
 * IBindingAction - Encapsulates bi-directional binding between two bindable
 * targets. The operations performed upon {@link #execute()} are implmentation
 * dependent.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IBindingAction extends IAction {
	
	/**
	 * Sets (adds) the given widget to this binding action.
	 * @param widget the widget to set
	 */
	void set(IBindableWidget<?> widget);

	/**
	 * Establishes the bindings. <br>
	 * NOTE: A bound widget must be set.
	 * @param widget the widget to bind
	 */
	void bind(IBindableWidget<?> widget);

	/**
	 * Unbinds what is currently bound performing necessary clean up.
	 * @param widget the widget to ubind
	 */
	void unbind(IBindableWidget<?> widget);
}
