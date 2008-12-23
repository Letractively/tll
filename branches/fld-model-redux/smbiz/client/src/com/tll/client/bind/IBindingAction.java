package com.tll.client.bind;

/**
 * IBindingAction - "Controls" a given component participating in a binding.
 * 
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author jpk
 * @param <B> The {@link IBindable} type.
 */
public interface IBindingAction<B extends IBindable> extends IAction<B> {

	/**
	 * Sets the initial values.
	 * @param bindable
	 */
	void set(B bindable);

	/**
	 * Establishes the bindings.
	 * @param bindable
	 */
	void bind(B bindable);

	/**
	 * Unbinds what is bound performing necessary clean up.
	 * @param bindable
	 */
	void unbind(B bindable);
}
