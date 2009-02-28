package com.tll.client.bind;

/**
 * IBindingAction - Encapsulates bi-directional binding between two bindable
 * targets. The operations performed upon {@link #execute()} are implmentation
 * dependent.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IBindingAction {

	/**
	 * Establishes the bindings.
	 */
	void bind();

	/**
	 * Unbinds what is currently bound performing necessary clean up.
	 */
	void unbind();

	/**
	 * Executes the action.
	 */
	void execute();
}
